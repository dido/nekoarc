/*
 * Copyright (c) 2019 Rafael R. Sevilla
 *
 * This file is part of NekoArc
 *
 * NekoArc is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.util.LongMap;
import com.stormwyrm.nekoarc.util.ObjectMap;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

import java.util.Arrays;

/**
 * Code generator.
 */
public class CodeGen extends ArcObject {
    public static final ArcObject TYPE = Symbol.intern("code");

    private byte[] geninst;
    private int pos, litpos;
    private LongMap<ArcObject> genlits;
    private final ObjectMap<String, ArcObject> codeLabelMap;
    private final ObjectMap<String, ArcObject> litLabelMap;
    private ArcObject ldls;
    private int start;

    /**
     * Create a new code generator
     */
    public CodeGen() {
        pos = 0;
        geninst = new byte[32];
        litpos = 0;
        genlits = new LongMap<>();
        codeLabelMap = new ObjectMap<>();
        litLabelMap = new ObjectMap<>();
        ldls = Nil.NIL;
        start = -1;
    }

    /**
     * Set up code generation.
     */
    public int startCode() {
        start = pos;
        ldls = Nil.NIL;
        return(start);
    }

    /**
     * Add an LDL instruction to the list.
     * @param ldlpos The position of the LDL instruction
     */
    public void ldl(int ldlpos) {
        // store the address as a one relative to the start
        ldls = new Cons(Fixnum.get(start - ldlpos), ldls);
    }

    /**
     * Finish up code generation. Creates a Code and will reset start and the ldl list.
     * @return A closure from the code just generated.
     */
    public Code endCode() {
        Code code = new Code(start, pos - start, ldls);
        start = -1;
        ldls = Nil.NIL;
        return(code);
    }

    /**
     * Write an instruction
     * @param b the opcode
     * @return offset at which the instruction was written
     */
    private int instwrite(byte b) {
        if (start < 0)
            throw new NekoArcException("code generated without startCode");
        if (pos >= geninst.length)
            geninst = Arrays.copyOf(geninst, geninst.length * 2);
        geninst[pos] = b;
        int tmppos = pos;
        pos++;
        return(tmppos);
    }

    /**
     * Load generated code into a virtual machine. Throws an exception if there are any unresolved labels.
     * @param vm the virtual machine to load into
     */
    public void load(VirtualMachine vm) {
    //  if (geninst.length > pos)
    //       geninst = Arrays.copyOf(geninst, pos);
    //  if (genlits.length > litpos)
    //    genlits = Arrays.copyOf(genlits, litpos);
        for (String key : codeLabelMap) {
            if (!(codeLabelMap.get(key) instanceof Fixnum))
                throw new NekoArcException("Unresolved code label " + key);
        }

        for (String key : litLabelMap) {
            if (!(litLabelMap.get(key) instanceof Fixnum))
                throw new NekoArcException("Unresolved literal label " + key);
        }
        vm.load(geninst, genlits);
    }

    /**
     * Emit an instruction with short (1-byte) parameters
     * @param op The opcode of the instruction
     * @param vals The parameters to the instruction
     * @return the offset at which the instruction was emitted
     */
    public int emits(byte op, int... vals) {
        int tmppos = instwrite(op);
        for (int val : vals)
            instwrite((byte)val);
        return(tmppos);
    }

    /**
     * Emit a 32-bit word
     * @param word the word to emit
     * @return the offset at which the word was emitted;
     */
    private int emitWord(int word) {
        int pos = this.pos;
        for (int i=0; i<4; i++) {
            instwrite((byte) (word & 0xff));
            word >>= 8;
        }
        return(pos);
    }

    /**
     * Emit an instruction with regular (4-byte) parameters
     * @param op The opcode of the instruction
     * @param vals The parameters of the instruction
     * @return the offset at which the instruction was emitted
     */
    public int emit(byte op, int... vals) {
        int pos = instwrite(op);
        for (int val : vals)
            emitWord(val);
        return(pos);
    }

    /**
     * Emit a branch instruction with a label operand. This will set the argument to the label if its position is
     * already known, or else add the position of the instruction to the label hash for later update when the label
     * value becomes known.
     * @param op The opcode of the instruction
     * @param label The label
     * @return The position of the instruction
     */
    public int bremit(byte op, String label) {
        int brpos = emit(op, 0);
        if (codeLabelMap.containsKey(label)) {
            ArcObject obj = codeLabelMap.get(label);
            if (obj instanceof Fixnum) {
                int labeldest = (int) ((Fixnum) obj).fixnum;
                patchRelativeBranch(brpos, labeldest);
            } else {
                Cons c = new Cons(Fixnum.get(brpos), obj);
                codeLabelMap.put(label, c);
            }
        } else {
            codeLabelMap.put(label, new Cons(Fixnum.get(brpos), Nil.NIL));
        }
        return(brpos);
    }

    /**
     * Emit an instruction with a literal operand. This will set the argument to the label if its position is already
     * known, or else add the position of the instruction to the label hash for later update when the label value
     * becomes known.
     * @param op The opcode of the instruction
     * @param label The label
     * @return The position of the instruction
     */
    public int lemit(byte op, String label) {
        int instpos = emit(op, 0);
        if (litLabelMap.containsKey(label)) {
            ArcObject obj = litLabelMap.get(label);
            if (obj instanceof Fixnum)
                setAtPos(instpos + 1, (int) ((Fixnum) obj).fixnum);
            else
                litLabelMap.put(label, new Cons(Fixnum.get(instpos), obj));
        } else {
            litLabelMap.put(label, new Cons(Fixnum.get(instpos), Nil.NIL));
        }
        return(instpos);
    }

    /**
     * Set a label position. This will also update any instructions that might have used the label.
     */
    public int label(String label, int labeldest) {
        if (codeLabelMap.containsKey(label)) {
            Cons labelList =  (Cons) codeLabelMap.get(label);
            for (ArcObject obj : labelList) {
                int brpos = (int) ((Fixnum)obj).fixnum;
                patchRelativeBranch(brpos, labeldest);
            }
        }
        codeLabelMap.put(label, Fixnum.get(labeldest));
        return(labeldest);
    }

    /**
     * Set a code byte at an offset
     * @param pos offset in the code
     * @param val the value to set
     * @return the position at which the change was made
     */
    public int setAtPos(int pos, int val) {
        for (int i=pos; i<pos+4; i++) {
            geninst[i] = (byte) (val & 0xff);
            val >>= 8;
        }
        return(pos);
    }

    /**
     * Get a code byte at an offset
     * @param pos offset in the code
     * @return the value of the byte at the offset
     * */
    public byte getAtPos(int pos) {
        return(geninst[pos]);
    }

    /**
     * Get the current position of the code pointer
     * @return the position of the code pointer
     */
    public int pos() {
        return(pos);
    }

    /**
     * Set the position of the code pointer
     * @param pos the new code pointer
     * @return the new code pointer
     */
    public int setPos(int pos) {
        return(this.pos = pos);
    }

    /**
     * Patch a relative branch instruction (CONT, JMP, JT, JF, etc.)
     * @param brpos Position of branch instruction
     * @param dest Intended target of branch instruction
     * @return calculated branch offset
     */
    public int patchRelativeBranch(int brpos, int dest)  {
        int newval = dest - (brpos + 5);
        setAtPos(brpos+1, newval);
        return(newval);
    }

    /**
     * Add a literal to the literals table
     * @param lit the literal to add
     * @return the offset in the literals table at which it was put
     */
    public int literal(ArcObject lit) {
        while (genlits.containsKey(litpos))
            litpos++;
        genlits.put(litpos, lit);
        return(litpos);
    }

    /**
     * Create a labelled literal
     * @param label The label of the literal
     * @param lit The literal
     * @return The position of the literal
     */
    public int literal(String label, ArcObject lit) {
        int lp = literal(lit);
        if (litLabelMap.containsKey(label)) {
            for (ArcObject ref : (Cons)litLabelMap.get(label))
                setAtPos((int) (((Fixnum)ref).fixnum + 1), lp);
        }
        litLabelMap.put(label, Fixnum.get(lp));
        return(lp);
    }

    /**
     * Get current position of literal pointer
     * @return the literal pointer
     */
    public int litPos() {
        return(litpos);
    }

    /**
     * Set the position of the literal pointer
     * @param litpos new literal position
     * @return new literal position
     */
    public int setLitPos(int litpos) {
        return(this.litpos = litpos);
    }

    /**
     * The type of a CodeGen
     * @return the type
     */
    @Override
    public ArcObject type() {
        return(TYPE);
    }

    /**
     * Convert to string
     * @return string rep
     */
    @Override
    public String toString() {
        return("#<CodeGen code: " + pos + " literals: " + litpos + ">");
    }
}

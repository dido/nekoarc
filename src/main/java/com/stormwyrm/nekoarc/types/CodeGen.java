/*  Copyright (C) 2018 Rafael R. Sevilla

    This file is part of NekoArc

    NekoArc is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 3 of the
    License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */


package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.vm.VirtualMachine;

import java.util.Arrays;

/**
 * Code generator.
 */
public class CodeGen extends ArcObject {
    public static final ArcObject TYPE = Symbol.intern("code");

    private byte[] geninst;
    private int pos, litpos;
    private ArcObject[] genlits;

    public CodeGen() {
        pos = 0;
        geninst = new byte[32];
        litpos = 0;
        genlits = new ArcObject[32];
    }

    private int instwrite(byte b) {
        if (pos >= geninst.length)
            geninst = Arrays.copyOf(geninst, geninst.length * 2);
        geninst[pos] = b;
        int tmppos = pos;
        pos++;
        return(tmppos);
    }

    public void load(VirtualMachine vm) {
    //  if (geninst.length > pos)
    //       geninst = Arrays.copyOf(geninst, pos);
    //  if (genlits.length > litpos)
    //    genlits = Arrays.copyOf(genlits, litpos);
        vm.load(geninst, genlits);
    }

    public int emits(byte op, int... vals) {
        int tmppos = instwrite(op);
        for (int val : vals)
            instwrite((byte)val);
        return(tmppos);
    }

    public int emit(byte op, int... vals) {
        int pos = instwrite(op);
        for (int val : vals) {
            for (int i=0; i<4; i++) {
                instwrite((byte) (val & 0xff));
                val >>= 8;
            }
        }
        return(pos);
    }

    public int setAtPos(int pos, int val) {
        for (int i=pos; i<pos+4; i++) {
            geninst[i] = (byte) (val & 0xff);
            val >>= 8;
        }
        return(pos);
    }

    public byte getAtPos(int pos) {
        return(geninst[pos]);
    }

    public int pos() {
        return(pos);
    }

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

    public int literal(ArcObject lit) {
        if (litpos >= genlits.length)
            genlits = Arrays.copyOf(genlits, genlits.length * 2);
        genlits[litpos] = lit;
        int tmppos = litpos;
        litpos++;
        return(tmppos);
    }

    public int litPos() {
        return(litpos);
    }

    public int setLitPos(int litpos) {
        return(this.litpos = litpos);
    }

    @Override
    public ArcObject type() {
        return(TYPE);
    }

    @Override
    public String toString() {
        return("#<CodeGen code: " + pos + " literals: " + litpos + ">");
    }
}

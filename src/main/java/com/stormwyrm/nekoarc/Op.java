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
package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public enum Op {
    NOP(0x00),
    PUSH(0x01),
    POP(0x02),
    RET(0x0d),
    NO(0x11),
    TRUE(0x12),
    NIL(0x13),
    HLT(0x14),
    ADD(0x15),
    SUB(0x16),
    MUL(0x17),
    DIV(0x18),
    CONS(0x19),
    CAR(0x1a),
    CDR(0x1b),
    SCAR(0x1c),
    SCDR(0x1d),
    IS(0x1f),
    DUP(0x22),
    CONSR(0x24),
    DCAR(0x26),
    DCDR(0x27),
    SPL(0x28),
    LDL(0x43, ArgType.REG_ARGS),
    LDI(0x44, ArgType.REG_ARGS),
    LDG(0x45, ArgType.REG_ARGS),
    STG(0x46, ArgType.REG_ARGS),
    APPLY(0x4c, ArgType.SMALL_ARGS),
    CLS(0x4d, ArgType.REG_ARGS),
    JMP(0x4e, ArgType.REG_ARGS),
    JT(0x4f, ArgType.REG_ARGS),
    JF(0x50, ArgType.REG_ARGS),
    JBND(0x51, ArgType.REG_ARGS),
    CONT(0x52, ArgType.REG_ARGS),
    MENV(0x65, ArgType.SMALL_ARGS),
    LDE0(0x69, ArgType.SMALL_ARGS),
    STE0(0x6a, ArgType.SMALL_ARGS),
    LDE(0x87, ArgType.SMALL_ARGS),
    STE(0x88, ArgType.SMALL_ARGS),
    ENV(0xca, ArgType.SMALL_ARGS),
    ENVR(0xcb, ArgType.SMALL_ARGS);

    private enum ArgType {
        NONE,
        REG_ARGS,
        SMALL_ARGS
    }

    /**
     * The opcode for the instruction
     */
    private final byte opcode;
    private final ArgType argType;

    /**
     * Create a new opcode for an instruction
     * @param opcode The opcode to generate
     * @param t The instruction type
     */
    Op(int opcode, ArgType t) {
        this.opcode = (byte) opcode;
        this.argType = t;
    }

    /**
     * Create a new opcode for an instruction.
     * @param opcode The opcode
     */
    Op(int opcode) {
        this(opcode, ArgType.NONE);
    }

    /**
     * Get the opcode for the instruction.
     * @return the opcode value
     */
    public byte opcode() {
        return(this.opcode);
    }

    /**
     * Emit code for the opcode
     * @param cg The code generator to emit to
     * @param args the arguments to the opcode
     * @return the code generator offset at which the instruction was emitted
     */
    public int emit(CodeGen cg, int... args) {
        // Determine the count of arguments from top two bits of opcode
        int argcount = (opcode >> 6) & 0x03;
        if (argcount != args.length) {
            throw new NekoArcException("wrong number arguments for instruction " + this.name()
                    + " (" + args.length + " given, " + argcount + " required");
        }
        if (argType == ArgType.SMALL_ARGS)
            return(cg.emits(opcode, args));
        return(cg.emit(opcode, args));
    }

    public int emit(VirtualMachine vm, int... args) {
        return(emit(vm.cg, args));
    }

    @Deprecated
    public int emits(CodeGen cg, int... args) {
        return(cg.emits(opcode, args));
    }

    @Deprecated
    public int emit(ArcThread thr, int... args) { return(emit(thr.vm.cg, args)); }

    @Deprecated
    public int emits(ArcThread thr, int... args) {
        return(emits(thr.vm.cg, args));
    }
}

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
   CLS(0x23),
   CONSR(0x24),
   DCAR(0x26),
   DCDR(0x27),
   SPL(0x28),
   LDL(0x43),
   LDI(0x44),
   LDG(0x45),
   STG(0x46),
   APPLY(0x4c),
   JMP(0x4e),
   JT(0x4f),
   JF(0x50),
   JBND(0x51),
   MENV(0x65),
   LDE0(0x69),
   STE0(0x6a),
   LDE(0x87),
   STE(0x88),
   CONT(0x89),
   ENV(0xca),
   ENVR(0xcb);

    private final byte opcode;

    Op(int opcode) {
        this.opcode = (byte) opcode;
    }

    public byte opcode() {
        return(this.opcode);
    }

    public int emit(CodeGen cg, int... args) {
        return(cg.emit(opcode, args));
    }

    public int emit(VirtualMachine vm, int... args) { return(emit(vm.cg, args)); }

    public int emits(CodeGen cg, int... args) {
        return(cg.emits(opcode, args));
    }

    public int emits(VirtualMachine vm, int... args) {
        return(emits(vm.cg, args));
    }
}

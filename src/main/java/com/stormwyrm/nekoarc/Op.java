package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public enum Op {
    NOP(0x00),
    PUSH(0x01),
    POP(0x02),
    RET(0x0d),
    TRUE(0x12),
    NIL(0x13),
    NO(0x14),
    HLT(0x15),
    ADD(0x16),
    SUB(0x17),
    MUL(0x18),
    DIV(0x19),
    CONS(0x1a),
    CAR(0x1b),
    CDR(0x1c),
    SCAR(0x1d),
    SCDR(0x1e),
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

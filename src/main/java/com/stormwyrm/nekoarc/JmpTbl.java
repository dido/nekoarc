package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.vm.INVALID;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.vm.instruction.*;

public class JmpTbl {
    private static final INVALID NOINST = new INVALID();
    public static final Instruction[] jmptbl = {
            new NOP(),        // 0x00
            new PUSH(),        // 0x01
            new POP(),        // 0x02
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            new RET(),        // 0x0d
            NOINST,
            NOINST,
            NOINST,
            new NO(),        // 0x11
            new TRUE(),        // 0x12
            new NIL(),        // 0x13
            new HLT(),        // 0x14
            new ADD(),        // 0x15
            new SUB(),        // 0x16
            new MUL(),        // 0x17
            new DIV(),        // 0x18
            new CONS(),        // 0x19
            new CAR(),        // 0x1a
            new CDR(),        // 0x1b
            new SCAR(),        // 0x1c
            new SCDR(),        // 0x1d
            NOINST,
            new IS(),        // 0x1f
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            new CONSR(),        // 0x24
            NOINST,
            new DCAR(),        // 0x26
            new DCDR(),        // 0x27
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            new LDL(),        // 0x43
            new LDI(),        // 0x44
            new LDG(),        // 0x45
            new STG(),        // 0x46
            new LDLP(),        // 0x47
            new LDIP(),        // 0x48
            new LDGP(),        // 0x49
            NOINST,
            NOINST,
            new APPLY(),        // 0x4c
            new CLS(),        // 0x4d
            new JMP(),        // 0x4e
            new JT(),        // 0x4f
            new JF(),        // 0x50
            new JBND(),        // 0x51
            new CONT(),        // 0x52
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            new MENV(),        // 0x65
            NOINST,
            NOINST,
            NOINST,
            new LDE0(),        // 0x69
            new STE0(),        // 0x6a
            new LDE0P(),        // 0x6b
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            new LDE(),        // 0x87
            new STE(),        // 0x88
            new LDEP(),        // 0x89
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            new ENV(),        // 0xca
            new ENVR(),        // 0xcb
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
            NOINST,
    };
}
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Op;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeGenTest {
    @Test
    public void test() {
        CodeGen cg = new CodeGen();
        int pos;

        pos = Op.NOP.emit(cg);
        assertEquals(0, pos);
        assertEquals(Op.NOP.opcode(), cg.getAtPos(pos));

        pos = Op.JMP.emit(cg, 0x12345678);
        assertEquals(1, pos);
        assertEquals(Op.JMP.opcode(), cg.getAtPos(pos));
        assertEquals(0x78, cg.getAtPos(pos+1));
        assertEquals(0x56, cg.getAtPos(pos+2));
        assertEquals(0x34, cg.getAtPos(pos+3));
        assertEquals(0x12, cg.getAtPos(pos+4));

        pos = Op.ENV.emit(cg, 1, 2, 3);
        assertEquals(6, pos);
        assertEquals(Op.ENV.opcode(), cg.getAtPos(pos));
        assertEquals(1, cg.getAtPos(pos+1));
        assertEquals(2, cg.getAtPos(pos+2));
        assertEquals(3, cg.getAtPos(pos+3));

        cg.setAtPos(2, 0x87654321);
        pos = 1;
        assertEquals(Op.JMP.opcode(), cg.getAtPos(pos));
        assertEquals(0x21, cg.getAtPos(pos+1));
        assertEquals(0x43, cg.getAtPos(pos+2));
        assertEquals(0x65, cg.getAtPos(pos+3));
        assertEquals((byte)0x87, cg.getAtPos(pos+4));
    }

}
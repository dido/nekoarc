package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class LDEPTest {
    private void testtmpl0(int idx, ArcObject expected) {
        // ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldep 0 idx; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                (byte) 0x89, 0x00, (byte)idx,
                0x14};
        CodeGen cg = new CodeGen();
        Op.LDI.emit(cg, 1);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 2);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 3);
        Op.PUSH.emit(cg);
        Op.ENV.emit(cg, 3, 1, 1);
        Op.LDEP.emit(cg, 0, idx);
        Op.HLT.emit(cg);
        assertEquals(cg.pos(), inst.length);
        for (int i=0; i<inst.length; i++)
            assertEquals(inst[i], cg.getAtPos(i));

        VirtualMachine vm = new VirtualMachine(cg);
        vm.load();
        ArcThread thr = new ArcThread(vm);
        thr.setargc(3);
        thr.setAcc(Nil.NIL);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertTrue(expected.is(thr.getAcc()));
        assertTrue(expected.is(thr.pop()));
        assertEquals(26, thr.getIP());
    }

    private void testtmpl0(int idx, int expected) {
        testtmpl0(idx, Fixnum.get(expected));
    }

    @Test
    public void test0() {
        testtmpl0(0, 1);
    }

    @Test
    public void test1() {
        testtmpl0(1, 2);
    }

    @Test
    public void test2() {
        testtmpl0(2, 3);
    }

    @Test
    public void test3() {
        testtmpl0(3, Unbound.UNBOUND);
    }

    @Test
    public void test4() {
        testtmpl0(4, Unbound.UNBOUND);
    }

    private void testtmpl(int depth, int idx, ArcObject expected) {
        // ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; env 1 0 2; lde depth idx; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                (byte) 0x89, (byte)depth, (byte)idx,
                0x14};
        CodeGen cg = new CodeGen();
        Op.LDI.emit(cg, 1);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 2);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 3);
        Op.PUSH.emit(cg);
        Op.ENV.emit(cg, 3, 1, 1);
        Op.LDI.emit(cg, 4);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 5);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 6);
        Op.PUSH.emit(cg);
        Op.ENV.emit(cg, 1, 0, 3);
        Op.LDEP.emit(cg, depth, idx);
        Op.HLT.emit(cg);
        assertEquals(cg.pos(), inst.length);
        for (int i=0; i<inst.length; i++)
            assertEquals(inst[i], cg.getAtPos(i));
        VirtualMachine vm = new VirtualMachine(cg);
        vm.load();
        ArcThread thr = new ArcThread(vm);
        thr.setargc(3);
        thr.setAcc(Nil.NIL);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertTrue(expected.is(thr.getAcc()));
        assertTrue(expected.is(thr.pop()));
        assertEquals(48, thr.getIP());
    }

    private void testtmpl(int depth, int idx, int expected) {
        testtmpl(depth, idx, Fixnum.get(expected));
    }

    @Test
    public void test1_0() {
        testtmpl(1,0, 1);
    }

    @Test
    public void test1_1() {
        testtmpl(1,1, 2);
    }

    @Test
    public void test1_2() {
        testtmpl(1, 2, 3);
    }

    @Test
    public void test1_3() {
        testtmpl(1,3, Unbound.UNBOUND);
    }

    @Test
    public void test1_4() {
        testtmpl(1,4, Unbound.UNBOUND);
    }

    @Test
    public void test0_0() {
        testtmpl(0,0, 4);
    }

    @Test
    public void test0_1() {
        testtmpl(0,1, 5);
    }

    @Test
    public void test0_2() {
        testtmpl(0,2, 6);
    }

    @Test
    public void test0_3() {
        testtmpl(0, 3, Unbound.UNBOUND);
    }

}
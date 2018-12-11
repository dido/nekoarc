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

public class LDE0PTest {
    private void testtmpl(int idx, ArcObject expected) {
        // ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; lde0 idx; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                (byte) 0x6b, (byte)idx,
                0x14};
        CodeGen cg = new CodeGen();
        Op.LDI.emit(cg, 1);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 2);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 3);
        Op.PUSH.emit(cg);
        Op.ENV.emit(cg, 3, 1, 1);
        Op.LDE0P.emit(cg, idx);
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
        assertEquals(25, thr.getIP());
    }

    private void testtmpl(int idx, int expected) {
        testtmpl(idx, Fixnum.get(expected));
    }

    @Test
    public void test0() {
        testtmpl(0, 1);
    }

    @Test
    public void test1() {
        testtmpl(1, 2);
    }

    @Test
    public void test2() {
        testtmpl(2, 3);
    }

    @Test
    public void test3() {
        testtmpl(3, Unbound.UNBOUND);
    }

    @Test
    public void test4() {
        testtmpl(4, Unbound.UNBOUND);
    }

}
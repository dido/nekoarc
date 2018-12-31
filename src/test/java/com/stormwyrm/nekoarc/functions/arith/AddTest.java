package com.stormwyrm.nekoarc.functions.arith;

import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddTest {
    @Test
    public void test0() {
        CodeGen cg = new CodeGen();
        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDG.emit(cg, "+");
        Op.APPLY.emit(cg, 0);
        Op.RET.emit(cg);
        cg.endCode();
        cg.literal("+", Symbol.intern("+"));
        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();
        vm.load();
        ArcThread thr = new ArcThread(vm);
        thr.vm.initSyms();

        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(0, ((Fixnum)thr.getAcc()).fixnum);
    }

    private void testtmpl(byte[] inst, int expected) {
        VirtualMachine vm = new VirtualMachine();
        vm.load(inst, new ArcObject[]{Symbol.intern("+")});
        vm.initSyms();
        ArcThread thr = new ArcThread(vm);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(expected, ((Fixnum)thr.getAcc()).fixnum);
    }

    @Test
    public void test1() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };

        testtmpl(inst, 1);
    }

    @Test
    public void test2() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        testtmpl(inst, 3);
    }

    @Test
    public void test3() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x44, 0x03, 0x00, 0x00, 0x00,            // ldi 3
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x03,                                // apply 3
                0x0d                                    // ret
        };
        testtmpl(inst, 6);
    }
}
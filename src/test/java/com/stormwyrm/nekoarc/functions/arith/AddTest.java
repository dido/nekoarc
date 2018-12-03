package com.stormwyrm.nekoarc.functions.arith;

import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddTest {
    @Test
    public void test0() {
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        Op.ENV.emits(thr, 0, 0, 0);
        Op.LDG.emit(thr, 0);
        Op.APPLY.emit(thr, 0);
        Op.RET.emit(thr);
        thr.vm.cg.literal(Symbol.intern("+"));

        thr.load();
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(0, ((Fixnum)thr.getAcc()).fixnum);
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
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern("+");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(1, ((Fixnum)thr.getAcc()).fixnum);
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
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern("+");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(3, ((Fixnum)thr.getAcc()).fixnum);
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
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern("+");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(6, ((Fixnum)thr.getAcc()).fixnum);
    }
}
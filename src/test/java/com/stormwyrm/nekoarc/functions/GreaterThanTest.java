package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Flonum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class GreaterThanTest {
    @Test
    public void test0() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x00,                                // apply 0
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test1Fixnum() {
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
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test1Flonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(3.14159);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test2aFixnum() {
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
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(Nil.NIL, thr.getAcc());
    }

    @Test
    public void test2bFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test2aFixnumFlonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(1e100);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(Nil.NIL, thr.getAcc());
    }

    @Test
    public void test2bFixnumFlonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(0.31831);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test2aFlonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                    // push
                0x43, 0x02, 0x00, 0x00, 0x00,            // ldl 2
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[3];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(1e100);
        literals[2] = new Flonum(1e101);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(Nil.NIL, thr.getAcc());
    }

    @Test
    public void test2bFlonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x43, 0x02, 0x00, 0x00, 0x00,            // ldl 2
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[3];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(1e100);
        literals[2] = new Flonum(1e-100);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test3aFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x44, 0x03, 0x00, 0x00, 0x00,            // ldi 3
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 3
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(Nil.NIL, thr.getAcc());
    }

    @Test
    public void test3bFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 3
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test3cFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x03, 0x00, 0x00, 0x00,            // ldi 3
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 3
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

}
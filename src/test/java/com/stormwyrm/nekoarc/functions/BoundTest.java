package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoundTest {
    @Test
    public void test1() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,   // env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x01,                                   // push
                0x45, 0x00, 0x00, 0x00, 0x00,           // ldg 0
                0x4c, 0x01,                             // apply 1
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern("bound");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test2() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,   // env 0 0 0
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                   // push
                0x45, 0x00, 0x00, 0x00, 0x00,           // ldg 0
                0x4c, 0x01,                             // apply 1
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern("bound");
        literals[1] = Symbol.intern("gs1722");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.main();
        assertEquals(Nil.NIL, thr.getAcc());
    }

}
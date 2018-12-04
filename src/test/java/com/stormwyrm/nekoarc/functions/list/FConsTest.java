package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class FConsTest {
    @Test
    public void test() {
        Builtin cons = FCons.getInstance();
        // Apply the above builtin
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x4c, 0x02,                              // apply 2
                0x0d                                     // ret
        };
        ArcThread vm = new ArcThread(1024);
        ArcObject[] literals = new ArcObject[1];
        literals[0] = cons;
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.main();
        assertFalse(vm.runnable());
        assertEquals(1, ((Fixnum)(vm.getAcc()).car()).fixnum);
        assertEquals(2, ((Fixnum)(vm.getAcc()).cdr()).fixnum);
    }
}
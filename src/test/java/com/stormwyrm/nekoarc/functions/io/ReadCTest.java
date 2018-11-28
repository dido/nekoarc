package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReadCTest {
    @Test
    public void test() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,   // env 0 0 0
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                   // push
                0x45, 0x00, 0x00, 0x00, 0x00,           // ldg 0
                0x4c, 0x01,                             // apply 1
                0x0d                                    // ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern("readc");
        literals[1] = new InString("蛟龍");
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertTrue(vm.getAcc() instanceof Rune);
        assertEquals(0x86df, ((Rune)vm.getAcc()).rune);
    }
}
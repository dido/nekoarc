package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Rune;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewStringTest {
    @Test
    public void test1() {
        byte inst[] = {(byte) 0xca, 0x00, 0x00, 0x00,   // env 0 0 0
                0x44, 0x05, 0x00, 0x00, 0x00,			// ldi 5
                0x01,                                   // push
                0x45, 0x00, 0x00, 0x00, 0x00,           // ldg 0
                0x4c, 0x01,                             // apply 1
                0x0d                                    // ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[1];
        literals[0] = Symbol.intern("newstring");
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertEquals("\u0000\u0000\u0000\u0000\u0000", vm.getAcc().toString());
    }

    @Test
    public void test2() {
        byte inst[] = {(byte) 0xca, 0x00, 0x00, 0x00,   // env 0 0 0
                0x44, 0x05, 0x00, 0x00, 0x00,			// ldi 5
                0x01,                                   // push
                0x43, 0x01, 0x00, 0x00, 0x00,			// ldl 1
                0x01,                                   // push
                0x45, 0x00, 0x00, 0x00, 0x00,           // ldg 0
                0x4c, 0x02,                             // apply 1
                0x0d                                    // ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[2];
        literals[0] = Symbol.intern("newstring");
        literals[1] = Rune.get(0x41);
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertEquals("AAAAA", vm.getAcc().toString());
    }
}
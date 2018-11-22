package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class AStringTest {
    @Test
    public void testApply() {
        AString str = new AString("日本語");
        // Apply the above string
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = str;
        vm.load(inst, literals, 0);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(0x672c, ((Rune)vm.getAcc()).rune);
        assertEquals("#\\本", vm.getAcc().toString());
    }

    @Test
    public void testUnicodeApply() {
        AString str = new AString("\uD83D\uDE1D\uD83D\uDE0E");
        // Apply the above string
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x00, 0x00, 0x00, 0x00,			// ldi 0
                0x01,									// push
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = str;
        vm.load(inst, literals, 0);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(0x1f61d, ((Rune)vm.getAcc()).rune);
        assertEquals("#\\\uD83D\uDE1D", vm.getAcc().toString());

    }
}
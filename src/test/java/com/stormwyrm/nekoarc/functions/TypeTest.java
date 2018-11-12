package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeTest {
    @Test
    public void testFixnum() {
        byte inst[] = {(byte) 0xca, 0x00, 0x00, 0x00,   // env 0 0 0
                0x44, 0x05, 0x00, 0x00, 0x00,           // ldi 5
                0x01,                                   // push
                0x45, 0x00, 0x00, 0x00, 0x00,           // ldg 0
                0x4c, 0x01,                             // apply 1
                0x0d                                    // ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[1];
        literals[0] = Symbol.intern("type");
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertEquals("fixnum", vm.getAcc().toString());    }
}
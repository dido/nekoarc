package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarTest {
    @Test
    public void test() {
        Builtin car = Car.getInstance();
        // Apply the above builtin
        // env 0 0 0; nil; push; ld1 1; cons; push ldl 0; apply 1; ret;
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x13,                                   // nil
                0x19,                                   // cons
                0x01,									// push
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = car;
        vm.load(inst, literals, 0);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(1, ((Fixnum)vm.getAcc()).fixnum);
    }

}
package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsTest {
    @Test
    public void test() {
        Builtin cons = Cons.CONS;
        // Apply the above builtin
        byte inst[] = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x4c, 0x02,                              // apply 2
                0x0d                                     // ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = cons;
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(1, ((Fixnum)(vm.getAcc()).car()).fixnum);
        assertEquals(2, ((Fixnum)(vm.getAcc()).cdr()).fixnum);
    }
}
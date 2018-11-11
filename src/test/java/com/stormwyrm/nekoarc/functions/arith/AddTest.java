package com.stormwyrm.nekoarc.functions.arith;

import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddTest {
    @Test
    public void test0() {
        Builtin add = Add.ADD;
        // Apply the above builtin
        // env 0 0 0; ldl 0; apply 0; ret;
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x00,								// apply 0
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = add;
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(0, ((Fixnum)vm.getAcc()).fixnum);
    }

    @Test
    public void test1() {
        Builtin add = Add.ADD;
        // Apply the above builtin
        // env 0 0 0; ldi 1; push; ldl 0; apply 1; ret;
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = add;
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(1, ((Fixnum)vm.getAcc()).fixnum);
    }

    @Test
    public void test2() {
        Builtin add = Add.ADD;
        // Apply the above builtin
        // env 0 0 0; ldi 1; push; ldi 2; push; ldl 0; apply 2; ret;
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x44, 0x02, 0x00, 0x00, 0x00,			// ldi 2
                0x01,									// push
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x02,								// apply 2
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = add;
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(3, ((Fixnum)vm.getAcc()).fixnum);
    }

    @Test
    public void test3() {
        Builtin add = Add.ADD;
        // Apply the above builtin
        // env 0 0 0; ldi 1; push; ldi 2; push; ldi 3; push; ldl 0; apply 3; ret;
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x44, 0x02, 0x00, 0x00, 0x00,			// ldi 2
                0x01,									// push
                0x44, 0x03, 0x00, 0x00, 0x00,			// ldi 3
                0x01,									// push
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x03,								// apply 3
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = add;
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(6, ((Fixnum)vm.getAcc()).fixnum);
    }
}
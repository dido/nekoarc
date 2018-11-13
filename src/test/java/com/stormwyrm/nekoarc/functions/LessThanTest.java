package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class LessThanTest {
    @Test
    public void test0() {
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x45, 0x00, 0x00, 0x00, 0x00,			// ldg 0
                0x4c, 0x00,								// apply 0
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[1];
        literals[0] = Symbol.intern("<");
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(True.T, vm.getAcc());
    }

    @Test
    public void test1Fixnum() {
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x45, 0x00, 0x00, 0x00, 0x00,			// ldg 0
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[1];
        literals[0] = Symbol.intern("<");
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(True.T, vm.getAcc());
    }

    @Test
    public void test2aFixnum() {
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x44, 0x02, 0x00, 0x00, 0x00,			// ldi 2
                0x01,									// push
                0x45, 0x00, 0x00, 0x00, 0x00,			// ldg 0
                0x4c, 0x02,								// apply 2
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[1];
        literals[0] = Symbol.intern("<");
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(True.T, vm.getAcc());
    }

    @Test
    public void test2bFixnum() {
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x44, 0x00, 0x00, 0x00, 0x00,			// ldi 0
                0x01,									// push
                0x45, 0x00, 0x00, 0x00, 0x00,			// ldg 0
                0x4c, 0x02,								// apply 2
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[1];
        literals[0] = Symbol.intern("<");
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(Nil.NIL, vm.getAcc());
    }
}
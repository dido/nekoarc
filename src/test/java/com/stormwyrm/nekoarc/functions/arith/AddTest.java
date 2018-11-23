package com.stormwyrm.nekoarc.functions.arith;

import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddTest {
    @Test
    public void test0() {
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        Op.ENV.emits(vm, 0, 0, 0);
        Op.LDG.emit(vm, 0);
        Op.APPLY.emit(vm, 0);
        Op.RET.emit(vm);
        vm.cg.literal(Symbol.intern("+"));

        vm.load();
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(0, ((Fixnum)vm.getAcc()).fixnum);
    }

    @Test
    public void test1() {
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
        literals[0] = Symbol.intern("+");
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(1, ((Fixnum)vm.getAcc()).fixnum);
    }

    @Test
    public void test2() {
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
        literals[0] = Symbol.intern("+");
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(3, ((Fixnum)vm.getAcc()).fixnum);
    }

    @Test
    public void test3() {
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x44, 0x02, 0x00, 0x00, 0x00,			// ldi 2
                0x01,									// push
                0x44, 0x03, 0x00, 0x00, 0x00,			// ldi 3
                0x01,									// push
                0x45, 0x00, 0x00, 0x00, 0x00,			// ldg 0
                0x4c, 0x03,								// apply 3
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[1];
        literals[0] = Symbol.intern("+");
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(6, ((Fixnum)vm.getAcc()).fixnum);
    }
}
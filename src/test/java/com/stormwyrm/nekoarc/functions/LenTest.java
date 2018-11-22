package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;
import com.stormwyrm.nekoarc.types.Cons;

import static org.junit.Assert.*;

public class LenTest {
    @Test
    public void testString() {
        AString str = new AString("日本語");
        Builtin len = Len.getInstance();
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x01,									// push
                0x43, 0x01, 0x00, 0x00, 0x00,			// ldl 1
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[2];
        literals[0] = str;
        literals[1] = len;
        vm.load(inst, literals, 0);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(3, ((Fixnum)vm.getAcc()).fixnum);
    }

    @Test
    public void testVector() {
        Vector vec = new Vector(5);
        Builtin len = Len.getInstance();
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x01,									// push
                0x43, 0x01, 0x00, 0x00, 0x00,			// ldl 1
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[2];
        literals[0] = vec;
        literals[1] = len;
        vm.load(inst, literals, 0);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(5, ((Fixnum)vm.getAcc()).fixnum);
    }

    @Test
    public void testCons() {
        Cons cons = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), new Cons(Fixnum.get(4), Nil.NIL))));
        Builtin len = Len.getInstance();
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x01,									// push
                0x43, 0x01, 0x00, 0x00, 0x00,			// ldl 1
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[2];
        literals[0] = cons;
        literals[1] = len;
        vm.load(inst, literals, 0);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(4, ((Fixnum)vm.getAcc()).fixnum);
    }
}
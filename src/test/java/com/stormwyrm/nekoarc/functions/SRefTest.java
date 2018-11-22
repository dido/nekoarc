package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class SRefTest {
    @Test
    public void testCons() {
        Cons cons = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), new Cons(Fixnum.get(4), Nil.NIL))));
        Builtin sref = SRef.getInstance();
        // Essentially (sref '(1 2 3 4) 5 2)
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x01,									// push
                0x44, 0x05, 0x00, 0x00, 0x00,			// ldi 5
                0x01,                                   // push
                0x44, 0x02, 0x00, 0x00, 0x00,			// ldi 2
                0x01,                                   // push
                0x43, 0x01, 0x00, 0x00, 0x00,			// ldl 1
                0x4c, 0x03,								// apply 3
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[2];
        literals[0] = cons;
        literals[1] = sref;
        vm.load(inst, literals, 0);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(5, ((Fixnum)vm.getAcc()).fixnum);
        assertEquals(1, ((Fixnum)cons.car()).fixnum);
        assertEquals(2, ((Fixnum)cons.cdr().car()).fixnum);
        assertEquals(5, ((Fixnum)cons.cdr().cdr().car()).fixnum);
        assertEquals(4, ((Fixnum)cons.cdr().cdr().cdr().car()).fixnum);
    }

    @Test
    public void testVector() {
        Vector v = new Vector(5);
        for (int i=0; i<5; i++)
            v.setIndex(i, Fixnum.get(i+1));
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x01,									// push
                0x44, 0x05, 0x00, 0x00, 0x00,			// ldi 5
                0x01,                                   // push
                0x44, 0x02, 0x00, 0x00, 0x00,			// ldi 2
                0x01,                                   // push
                0x45, 0x01, 0x00, 0x00, 0x00,			// ldg 1
                0x4c, 0x03,								// apply 3
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        ArcObject literals[] = new ArcObject[2];
        literals[0] = v;
        literals[1] = Symbol.intern("sref");
        vm.load(inst, literals, 0);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(5, ((Fixnum)vm.getAcc()).fixnum);
        assertEquals(1, ((Fixnum)v.index(0)).fixnum);
        assertEquals(2, ((Fixnum)v.index(1)).fixnum);
        assertEquals(5, ((Fixnum)v.index(2)).fixnum);
        assertEquals(4, ((Fixnum)v.index(3)).fixnum);
    }
}
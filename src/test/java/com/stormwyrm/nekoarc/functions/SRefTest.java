package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class SRefTest {
    @Test
    public void testCons() {
        Cons cons = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), new Cons(Fixnum.get(4), Nil.NIL))));
        SRef sref = SRef.SREF;
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
        vm.load(inst, 0, literals);
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
}
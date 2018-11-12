package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScarTest {
    @Test
    public void test() {
        Cons cons = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), new Cons(Fixnum.get(4), Nil.NIL))));
        Builtin scar = Scar.getInstance();
        // Essentially (scar '(1 2 3 4) 5)
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x01,									// push
                0x44, 0x05, 0x00, 0x00, 0x00,           // ldi 5
                0x01,                                   // push
                0x43, 0x01, 0x00, 0x00, 0x00,			// ldl 1
                0x4c, 0x02,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[2];
        literals[0] = cons;
        literals[1] = scar;
        vm.load(inst, 0, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(5, ((Fixnum)vm.getAcc()).fixnum);
        assertEquals(5, ((Fixnum)cons.car()).fixnum);
        assertEquals(2, ((Fixnum)cons.cdr().car()).fixnum);
    }
}
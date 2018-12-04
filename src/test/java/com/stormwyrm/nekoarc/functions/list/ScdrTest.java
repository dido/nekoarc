package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScdrTest {
    @Test
    public void test() {
        Cons cons = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), new Cons(Fixnum.get(4), Nil.NIL))));
        ArcObject scdr = Scdr.getInstance();
        // Essentially (scdr '(1 2 3 4) nil)
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x01,                                    // push
                0x13,                                   // nil
                0x01,                                   // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x4c, 0x02,                                // apply 1
                0x0d                                    // ret
        };
        ArcThread vm = new ArcThread(1024);
        ArcObject[] literals = new ArcObject[2];
        literals[0] = cons;
        literals[1] = scdr;
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.main();
        assertFalse(vm.runnable());
        assertEquals(Nil.NIL, vm.getAcc());
        assertEquals(1, ((Fixnum) cons.car()).fixnum);
        assertEquals(Nil.NIL, cons.cdr());
    }
}
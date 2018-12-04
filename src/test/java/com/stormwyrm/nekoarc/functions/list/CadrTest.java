package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class CadrTest {
    @Test
    public void test() {
        Cons cons = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), new Cons(Fixnum.get(4), Nil.NIL))));
        Builtin cadr = Cadr.getInstance();
        // Essentially (cadr '(1 2 3 4))
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x01,                                    // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };
        ArcThread vm = new ArcThread(1024);
        ArcObject[] literals = new ArcObject[2];
        literals[0] = cons;
        literals[1] = cadr;
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.main();
        assertFalse(vm.runnable());
        assertEquals(2, ((Fixnum)vm.getAcc()).fixnum);
    }

}
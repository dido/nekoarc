/*
 * Copyright (c) 2019 Rafael R. Sevilla
 *
 * This file is part of NekoArc
 *
 * NekoArc is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class SRefTest {
    @Test
    public void testCons() {
        Cons cons = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), new Cons(Fixnum.get(4), Nil.NIL))));
        Builtin sref = SRef.getInstance();
        // Essentially (sref '(1 2 3 4) 5 2)
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x01,                                    // push
                0x44, 0x05, 0x00, 0x00, 0x00,            // ldi 5
                0x01,                                   // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                   // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x4c, 0x03,                                // apply 3
                0x0d                                    // ret
        };
        ArcThread vm = new ArcThread(1024);
        ArcObject[] literals = new ArcObject[2];
        literals[0] = cons;
        literals[1] = sref;
        vm.load(inst, literals);
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
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x01,                                    // push
                0x44, 0x05, 0x00, 0x00, 0x00,            // ldi 5
                0x01,                                   // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                   // push
                0x45, 0x01, 0x00, 0x00, 0x00,            // ldg 1
                0x4c, 0x03,                                // apply 3
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = v;
        literals[1] = Symbol.intern("sref");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(5, ((Fixnum)thr.getAcc()).fixnum);
        assertEquals(1, ((Fixnum)v.index(0)).fixnum);
        assertEquals(2, ((Fixnum)v.index(1)).fixnum);
        assertEquals(5, ((Fixnum)v.index(2)).fixnum);
        assertEquals(4, ((Fixnum)v.index(3)).fixnum);
    }
}
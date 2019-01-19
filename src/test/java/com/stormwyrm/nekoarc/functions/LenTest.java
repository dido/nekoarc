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
import com.stormwyrm.nekoarc.types.Cons;

import static org.junit.Assert.*;

public class LenTest {
    @Test
    public void testString() {
        AString str = new AString("日本語");
        Builtin len = Len.getInstance();
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x01,                                    // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };
        ArcThread vm = new ArcThread(1024);
        ArcObject[] literals = new ArcObject[2];
        literals[0] = str;
        literals[1] = len;
        vm.load(inst, literals);
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
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x01,                                    // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };
        ArcThread vm = new ArcThread(1024);
        ArcObject[] literals = new ArcObject[2];
        literals[0] = vec;
        literals[1] = len;
        vm.load(inst, literals);
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
        literals[1] = len;
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(4, ((Fixnum)vm.getAcc()).fixnum);
    }
}
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

package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarTest {
    @Test
    public void test() {
        Builtin car = Car.getInstance();
        // Apply the above builtin
        // env 0 0 0; nil; push; ld1 1; cons; push ldl 0; apply 1; ret;
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x13,                                   // nil
                0x19,                                   // cons
                0x01,                                    // push
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };
        ArcThread vm = new ArcThread(1024);
        ArcObject[] literals = new ArcObject[1];
        literals[0] = car;
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(1, ((Fixnum)vm.getAcc()).fixnum);
    }

}
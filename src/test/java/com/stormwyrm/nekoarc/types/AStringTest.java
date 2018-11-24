/*  Copyright (C) 2018 Rafael R. Sevilla

    This file is part of NekoArc

    NekoArc is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 3 of the
    License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class AStringTest {
    @Test
    public void testApply() {
        AString str = new AString("日本語");
        // Apply the above string
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,			// ldi 1
                0x01,									// push
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = str;
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(0x672c, ((Rune)vm.getAcc()).rune);
        assertEquals("#\\本", vm.getAcc().toString());
    }

    @Test
    public void testUnicodeApply() {
        AString str = new AString("\uD83D\uDE1D\uD83D\uDE0E");
        // Apply the above string
        byte inst[] = {(byte)0xca, 0x00, 0x00, 0x00,	// env 0 0 0
                0x44, 0x00, 0x00, 0x00, 0x00,			// ldi 0
                0x01,									// push
                0x43, 0x00, 0x00, 0x00, 0x00,			// ldl 0
                0x4c, 0x01,								// apply 1
                0x0d									// ret
        };
        VirtualMachine vm = new VirtualMachine(1024);
        ArcObject literals[] = new ArcObject[1];
        literals[0] = str;
        vm.load(inst, literals);
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(0x1f61d, ((Rune)vm.getAcc()).rune);
        assertEquals("#\\\uD83D\uDE1D", vm.getAcc().toString());

    }

    @Test
    public void testSref() {
        AString str = new AString("蛟竜");
        ArcObject obj = str.sref(Rune.get(0x9f8d), Fixnum.get(1));
        assertEquals("蛟龍", str.toString());
        assertTrue(obj.is(Rune.get(0x9f8d)));
    }
}
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

package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class SCARtest
{
	@Test
	public void test()
	{
		// ldl 0; push; ldi 3; scar; hlt 
		byte[] inst = {0x43, 0x00, 0x00, 0x00, 0x00,
				0x01,
				0x44, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1c,
				0x14};
		ArcObject[] literals = new ArcObject[1];
		literals[0] = new Cons(Fixnum.get(1), Fixnum.get(2));
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(3, ((Fixnum) vm.getAcc().car()).fixnum);
		assertEquals(2, ((Fixnum) vm.getAcc().cdr()).fixnum);
		assertEquals(3, ((Fixnum) literals[0].car()).fixnum);
		assertEquals(2, ((Fixnum) literals[0].cdr()).fixnum);
		assertEquals(13, vm.getIP());
	}

}

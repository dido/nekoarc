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
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class STGtest
{

	@Test
	public void test()
	{
		// ldi 0x7f, stg 0; hlt
		byte[] inst = {0x44, 0x7f, 0x00, 0x00, 0x00,
				0x46, 0x00, 0x00, 0x00, 0x00, 0x14};
		ArcObject[] literals = new ArcObject[1];
		Symbol sym = (Symbol)Symbol.intern("foo");
		literals[0] = sym;
		VirtualMachine vm = new VirtualMachine();
		vm.load(inst, literals);
		ArcThread thr = new ArcThread(vm,1024);
		thr.setAcc(Nil.NIL);
		vm.bind(sym, Fixnum.get(1234));
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(0x7f, ((Fixnum)thr.getAcc()).fixnum);
		assertEquals(0x7f, ((Fixnum)vm.value(sym)).fixnum);
		assertEquals(11, thr.getIP());
	}

}

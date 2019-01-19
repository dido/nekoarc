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

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class ENVtest
{
	@Test
	public void test()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setargc(3);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(Fixnum.get(1), vm.getenv(0, 0));
		assertEquals(Fixnum.get(2), vm.getenv(0, 1));
		assertEquals(Fixnum.get(3), vm.getenv(0, 2));
		assertTrue(Unbound.UNBOUND.is(vm.getenv(0, 3)));
		assertTrue(Unbound.UNBOUND.is(vm.getenv(0, 4)));
		assertEquals(23, vm.getIP());
	}

	@Test
	public void testTooFewArgs()
	{
		// ldi 1; push; env 3 1 1; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setargc(1);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		try {
			vm.run();
			fail("exception not thrown");
		} catch (NekoArcException e) {
			assertEquals("too few arguments, at least 3 required, 1 passed", e.getMessage());
		}
	}

	@Test
	public void testTooManyArgs()
	{
		// ldi 1; push; ldi 1; push; ldi 1; push; ldi 1; push; ldi 1; push; env 3 1 1; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setargc(5);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		try {
			vm.run();
			fail("exception not thrown");
		} catch (NekoArcException e) {
			assertEquals("too many arguments, at most 4 allowed, 5 passed", e.getMessage());
		}
	}
}

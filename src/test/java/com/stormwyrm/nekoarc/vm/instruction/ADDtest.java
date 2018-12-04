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
package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Flonum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class ADDtest {

	@Test
	public void testFixnumPlusFixnum() throws NekoArcException
	{
		// ldi 1; push; ldi 2; add; hlt
        byte[] inst = {0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x15,
                0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(3, ((Fixnum)vm.getAcc()).fixnum);
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testFixnumPlusFlonum() throws NekoArcException
	{
		// ldi 1; push; ldl 0; add; hlt
        byte[] inst = {0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x15,
                0x14};
        ArcObject[] literals = new ArcObject[1];
		literals[0] = new Flonum(3.14);
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(4.14, ((Flonum)vm.getAcc()).flonum, 1e-6);
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testFlonumPlusFixnum() throws NekoArcException
	{
		// ldl 0; push; ldi 1; add; hlt
        byte[] inst = {0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x15,
                0x14};
        ArcObject[] literals = new ArcObject[1];
		literals[0] = new Flonum(3.14);
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(4.14, ((Flonum)vm.getAcc()).flonum, 1e-6);
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testFlonumPlusFlonum() throws NekoArcException
	{
		// ldl 0; push; ldl 1; add; hlt
        byte[] inst = {0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x15,
                0x14};
        ArcObject[] literals = new ArcObject[2];
		literals[0] = new Flonum(3.14);
		literals[1] = new Flonum(2.71);
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(5.85, ((Flonum)vm.getAcc()).flonum, 1e-6);
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testStringPlusString() throws NekoArcException
	{
		// ldl 0; push; ldl 1; add; hlt
        byte[] inst = {0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x15,
                0x14};
        ArcObject[] literals = new ArcObject[2];
		literals[0] = new AString("abc");
		literals[1] = new AString("def");
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals("abcdef", vm.getAcc().toString());
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testStringPlusFixnum() throws NekoArcException
	{
		// ldl 0; push; ldi 1; add; hlt
        byte[] inst = {0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x15,
                0x14};
        ArcObject[] literals = new ArcObject[1];
		literals[0] = new AString("foo");
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals("foo1", vm.getAcc().toString());
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testStringPlusFlonum() throws NekoArcException
	{
		// ldl 0; push; ldl 1; add; hlt
        byte[] inst = {0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x15,
                0x14};
        ArcObject[] literals = new ArcObject[2];
		literals[0] = new AString("foo");
		literals[1] = new Flonum(1.1);
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals("foo1.1", vm.getAcc().toString());
		assertEquals(13, vm.getIP());
	}
}

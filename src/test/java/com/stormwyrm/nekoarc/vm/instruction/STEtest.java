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
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class STEtest
{
	@Test
	public void test0_0()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 0 0; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x00, 0x00,
                0x14};
		VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm, 1024);
		vm.load(inst);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getenv(1, 0));
		assertEquals(Fixnum.get(2), thr.getenv(1, 1));
		assertEquals(Fixnum.get(3), thr.getenv(1, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 3)));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 4)));
//		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertTrue(thr.getenv(0, 0).is(Nil.NIL));
		assertEquals(Fixnum.get(5), thr.getenv(0, 1));
		assertEquals(Fixnum.get(6), thr.getenv(0, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(0, 3)));
		assertEquals(49, thr.getIP());
	}

	@Test
	public void test0_1()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 0 1; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x00, 0x01,
                0x14};
        VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm, 1024);
		vm.load(inst);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getenv(1, 0));
		assertEquals(Fixnum.get(2), thr.getenv(1, 1));
		assertEquals(Fixnum.get(3), thr.getenv(1, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 3)));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 4)));
		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertTrue(thr.getenv(0, 1).is(Nil.NIL));
		assertEquals(Fixnum.get(6), thr.getenv(0, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(0, 3)));
		assertEquals(49, thr.getIP());
	}

	@Test
	public void test0_2()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 0 2; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x00, 0x02,
                0x14};
		VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm, 1024);
		vm.load(inst);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getenv(1, 0));
		assertEquals(Fixnum.get(2), thr.getenv(1, 1));
		assertEquals(Fixnum.get(3), thr.getenv(1, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 3)));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 4)));
		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertEquals(Fixnum.get(5), thr.getenv(0, 1));
		assertTrue(thr.getenv(0, 2).is(Nil.NIL));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(0, 3)));
		assertEquals(49, thr.getIP());
	}

	@Test
	public void test0_3()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 0 3; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x00, 0x03,
                0x14};
        VirtualMachine vm = new VirtualMachine();
		vm.load(inst);
		ArcThread thr = new ArcThread(vm,1024);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getenv(1, 0));
		assertEquals(Fixnum.get(2), thr.getenv(1, 1));
		assertEquals(Fixnum.get(3), thr.getenv(1, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 3)));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 4)));
		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertEquals(Fixnum.get(5), thr.getenv(0, 1));
		assertEquals(Fixnum.get(6), thr.getenv(0, 2));
		assertTrue(thr.getenv(0, 3).is(Nil.NIL));
		assertEquals(49, thr.getIP());
	}

	@Test
	public void test1_0()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 1 0; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x01, 0x00,
                0x14};
        VirtualMachine vm = new VirtualMachine();
		vm.load(inst);
		ArcThread thr = new ArcThread(vm,1024);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertTrue(thr.getenv(1, 0).is(Nil.NIL));
		assertEquals(Fixnum.get(2), thr.getenv(1, 1));
		assertEquals(Fixnum.get(3), thr.getenv(1, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 3)));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 4)));
		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertEquals(Fixnum.get(5), thr.getenv(0, 1));
		assertEquals(Fixnum.get(6), thr.getenv(0, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(0, 3)));
		assertEquals(49, thr.getIP());
	}

	@Test
	public void test1_1()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 1 1; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x01, 0x01,
                0x14};
        VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm,1024);
		vm.load(inst);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getenv(1, 0));
		assertTrue(thr.getenv(1, 1).is(Nil.NIL));
		assertEquals(Fixnum.get(3), thr.getenv(1, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 3)));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 4)));
		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertEquals(Fixnum.get(5), thr.getenv(0, 1));
		assertEquals(Fixnum.get(6), thr.getenv(0, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(0, 3)));
		assertEquals(49, thr.getIP());
	}

	@Test
	public void test1_2()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 1 2; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x01, 0x02,
                0x14};
        VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm,1024);
		vm.load(inst);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getenv(1, 0));
		assertEquals(Fixnum.get(2), thr.getenv(1, 1));
		assertTrue(thr.getenv(1, 2).is(Nil.NIL));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 3)));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 4)));
		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertEquals(Fixnum.get(5), thr.getenv(0, 1));
		assertEquals(Fixnum.get(6), thr.getenv(0, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(0, 3)));
		assertEquals(49, thr.getIP());
	}

	@Test
	public void test1_3()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 1 3; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x01, 0x03,
                0x14};
        VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm,1024);
		vm.load(inst);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getenv(1, 0));
		assertEquals(Fixnum.get(2), thr.getenv(1, 1));
		assertEquals(Fixnum.get(3), thr.getenv(1, 2));
		assertTrue(thr.getenv(1, 3).is(Nil.NIL));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 4)));
		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertEquals(Fixnum.get(5), thr.getenv(0, 1));
		assertEquals(Fixnum.get(6), thr.getenv(0, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(0, 3)));
		assertEquals(49, thr.getIP());
	}

	@Test
	public void test1_4()
	{
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; ldi 5; push; ldi 6; push; env 1 0 3; nil; ste 1 4; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                0x44, 0x04, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x05, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x06, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x01, 0x00, 0x03,
                0x13,
                (byte) 0x88, 0x01, 0x04,
                0x14};
        VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm,1024);
		vm.load(inst);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getenv(1, 0));
		assertEquals(Fixnum.get(2), thr.getenv(1, 1));
		assertEquals(Fixnum.get(3), thr.getenv(1, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(1, 3)));
		assertTrue(thr.getenv(1, 4).is(Nil.NIL));
		assertEquals(Fixnum.get(4), thr.getenv(0, 0));
		assertEquals(Fixnum.get(5), thr.getenv(0, 1));
		assertEquals(Fixnum.get(6), thr.getenv(0, 2));
		assertTrue(Unbound.UNBOUND.is(thr.getenv(0, 3)));
		assertEquals(49, thr.getIP());
	}

}

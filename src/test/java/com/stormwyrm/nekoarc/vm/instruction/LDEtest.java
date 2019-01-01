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

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class LDEtest {
	@Test
	public void test0() {
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; lde 0 0; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                (byte) 0x87, 0x00, 0x00,
                0x14};
        CodeGen cg = new CodeGen();
        cg.startCode();
        Op.LDI.emit(cg, 1);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 2);
        Op.PUSH.emit(cg);
        Op.LDI.emit(cg, 3);
        Op.PUSH.emit(cg);
        Op.ENV.emit(cg, 3, 1, 1);
        Op.LDE.emit(cg, 0, 0);
        Op.HLT.emit(cg);
        cg.endCode();
        assertEquals(cg.pos(), inst.length);
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));

		VirtualMachine vm = new VirtualMachine(cg);
		vm.load();
		ArcThread thr = new ArcThread(vm);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(1), thr.getAcc());
		assertEquals(26, thr.getIP());
	}

	@Test
	public void test1() {
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 2; lde 0 1; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                (byte) 0x87, 0x00, 0x01,
                0x14};
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.LDI.emit(cg, 1);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 2);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 3);
		Op.PUSH.emit(cg);
		Op.ENV.emit(cg, 3, 1, 1);
		Op.LDE.emit(cg, 0, 1);
		Op.HLT.emit(cg);
		cg.endCode();
		assertEquals(cg.pos(), inst.length);
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));
		VirtualMachine vm = new VirtualMachine(cg);
		vm.load();
		ArcThread thr = new ArcThread(vm);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(2), thr.getAcc());
		assertEquals(26, thr.getIP());
	}

	@Test
	public void test2() {
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 2; lde 0 0; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                (byte) 0x87, 0x00, 0x02,
                0x14};
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.LDI.emit(cg, 1);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 2);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 3);
		Op.PUSH.emit(cg);
		Op.ENV.emit(cg, 3, 1, 1);
		Op.LDE.emit(cg, 0, 2);
		Op.HLT.emit(cg);
		cg.endCode();
		assertEquals(cg.pos(), inst.length);
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));
		VirtualMachine vm = new VirtualMachine(cg);
		vm.load();
		ArcThread thr = new ArcThread(vm);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(3), thr.getAcc());
		assertEquals(26, thr.getIP());
	}

	@Test
	public void test3() {
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; lde 0 0; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                (byte) 0x87, 0x00, 0x03,
                0x14};
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.LDI.emit(cg, 1);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 2);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 3);
		Op.PUSH.emit(cg);
		Op.ENV.emit(cg, 3, 1, 1);
		Op.LDE.emit(cg, 0, 3);
		Op.HLT.emit(cg);
		cg.endCode();
		assertEquals(cg.pos(), inst.length);
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));
		VirtualMachine vm = new VirtualMachine(cg);
		vm.load();
		ArcThread thr = new ArcThread(vm);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertTrue(thr.getAcc().is(Unbound.UNBOUND));
		assertEquals(26, thr.getIP());
	}

	@Test
	public void test4() {
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 2; lde 0 0; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x03, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xca, 0x03, 0x01, 0x01,
                (byte) 0x87, 0x00, 0x04,
                0x14};
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.LDI.emit(cg, 1);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 2);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 3);
		Op.PUSH.emit(cg);
		Op.ENV.emit(cg, 3, 1, 1);
		Op.LDE.emit(cg, 0, 4);
		Op.HLT.emit(cg);
		cg.endCode();
		assertEquals(cg.pos(), inst.length);
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));
		VirtualMachine vm = new VirtualMachine(cg);
		vm.load();
		ArcThread thr = new ArcThread(vm);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertTrue(thr.getAcc().is(Unbound.UNBOUND));
		assertEquals(26, thr.getIP());
	}

	private void testtmpl(int depth, int idx, ArcObject expected) {
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; ldi 4; push; env 1 0 2; lde depth idx; hlt;
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
				(byte) 0x87, (byte)depth, (byte)idx,
				0x14};
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.LDI.emit(cg, 1);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 2);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 3);
		Op.PUSH.emit(cg);
		Op.ENV.emit(cg, 3, 1, 1);
		Op.LDI.emit(cg, 4);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 5);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 6);
		Op.PUSH.emit(cg);
		Op.ENV.emit(cg, 1, 0, 3);
		Op.LDE.emit(cg, depth, idx);
		Op.HLT.emit(cg);
		cg.endCode();
		assertEquals(cg.pos(), inst.length);
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));
		VirtualMachine vm = new VirtualMachine(cg);
		vm.load();
		ArcThread thr = new ArcThread(vm);
		thr.setargc(3);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertTrue(expected.is(thr.getAcc()));
		assertEquals(48, thr.getIP());
	}

	private void testtmpl(int depth, int idx, int expected) {
		testtmpl(depth, idx, Fixnum.get(expected));
	}

	@Test
	public void test1_0() {
		testtmpl(1,0, 1);
	}

	@Test
	public void test1_1() {
		testtmpl(1,1, 2);
	}

	@Test
	public void test1_2() {
		testtmpl(1, 2, 3);
	}

	@Test
	public void test1_3() {
		testtmpl(1,3, Unbound.UNBOUND);
	}

	@Test
	public void test1_4() {
		testtmpl(1,4, Unbound.UNBOUND);
	}

	@Test
	public void test0_0() {
		testtmpl(0,0, 4);
	}

	@Test
	public void test0_1() {
		testtmpl(0,1, 5);
	}

	@Test
	public void test0_2() {
		testtmpl(0,2, 6);
	}

	@Test
	public void test0_3() {
		testtmpl(0, 3, Unbound.UNBOUND);
	}

}

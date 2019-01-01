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

public class LDE0test {

	private void testtmpl(int idx, ArcObject expected) {
		// ldi 1; push; ldi 2; push; ldi 3; push; env 3 1 1; lde0 idx; hlt;
		byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
				0x01,
				0x44, 0x02, 0x00, 0x00, 0x00,
				0x01,
				0x44, 0x03, 0x00, 0x00, 0x00,
				0x01,
				(byte) 0xca, 0x03, 0x01, 0x01,
				(byte) 0x69, (byte)idx,
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
		Op.LDE0.emit(cg, idx);
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
		assertEquals(25, thr.getIP());
	}

	private void testtmpl(int idx, int expected) {
		testtmpl(idx, Fixnum.get(expected));
	}

	@Test
	public void test0() {
		testtmpl(0, 1);
	}

	@Test
	public void test1() {
		testtmpl(1, 2);
	}

	@Test
	public void test2() {
		testtmpl(2, 3);
	}

	@Test
	public void test3() {
		testtmpl(3, Unbound.UNBOUND);
	}

	@Test
	public void test4() {
		testtmpl(4, Unbound.UNBOUND);
	}

}

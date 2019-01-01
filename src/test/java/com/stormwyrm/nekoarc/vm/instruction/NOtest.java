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
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class NOtest {

	private interface MyCodeGen {
		void codeGen(CodeGen cg);
	}

	private void testtmpl(byte[] inst, MyCodeGen mcg, ArcObject expected) {
		CodeGen cg = new CodeGen();
		mcg.codeGen(cg);
		assertEquals(inst.length, cg.pos());
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));
		VirtualMachine vm = new VirtualMachine(cg);
		vm.load();
		ArcThread t = new ArcThread(vm);
		t.setAcc(Fixnum.get(1234));
		assertTrue(t.runnable());
		t.run();
		assertFalse(t.runnable());
		assertTrue(t.getAcc().is(expected));
	}

	@Test
	public void testNoNil() {
		// nil; no; hlt
		byte[] inst = {0x13, 0x11, 0x14};
		MyCodeGen mcg = (cg) -> {
			cg.startCode();
			Op.NIL.emit(cg);
			Op.NO.emit(cg);
			Op.HLT.emit(cg);
			cg.endCode();
		};
		testtmpl(inst, mcg, True.T);
	}
	
	@Test
	public void testNoTrue() {
		// true; no; hlt
		byte[] inst = {0x12, 0x11, 0x14};
		MyCodeGen mcg = (cg) -> {
			cg.startCode();
			Op.TRUE.emit(cg);
			Op.NO.emit(cg);
			Op.HLT.emit(cg);
			cg.endCode();
		};
		testtmpl(inst, mcg, Nil.NIL);
	}

	@Test
	public void testNoNumber()
	{
		// ldi 2; no; hlt
		byte[] inst = {0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x11, 0x14};
		MyCodeGen mcg = (cg) -> {
			cg.startCode();
			Op.LDI.emit(cg, 2);
			Op.NO.emit(cg);
			Op.HLT.emit(cg);
			cg.endCode();
		};
		testtmpl(inst, mcg, Nil.NIL);
	}

}

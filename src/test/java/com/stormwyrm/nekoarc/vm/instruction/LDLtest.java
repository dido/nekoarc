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
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class LDLtest {
	@Test
	public void test() throws NekoArcException {
		// ldl 0; hlt
        byte[] inst = {0x43, 0x00, 0x00, 0x00, 0x00, 0x14};
		CodeGen cg = new CodeGen();
		Op.LDL.emit(cg, 0);
		Op.HLT.emit(cg);
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));

		cg.literal(Symbol.intern("foo"));

		VirtualMachine vm = new VirtualMachine(cg);
		ArcThread thr = new ArcThread(vm, 1024);
		vm.load();
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Symbol.intern("foo"), thr.getAcc());
		assertEquals(6, thr.getIP());
	}
}

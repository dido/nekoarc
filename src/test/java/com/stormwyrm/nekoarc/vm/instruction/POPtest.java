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
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class POPtest
{
	@Test
	public void test() throws NekoArcException {
		// ldi 555729757; push; ldi -555729757; pop; hlt
        byte[] inst = {0x44, (byte) 0x5d, (byte) 0xc3, (byte) 0x1f, (byte) 0x21, 0x01,
                0x44, (byte) 0xa3, (byte) 0x3c, (byte) 0xe0, (byte) 0xde, 0x02, 0x14};
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.LDI.emit(cg, 555729757);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, -555729757);
		Op.POP.emit(cg);
		Op.HLT.emit(cg);
		cg.endCode();
		assertEquals(inst.length, cg.pos());
		for (int i=0; i<inst.length; i++)
			assertEquals(inst[i], cg.getAtPos(i));
		VirtualMachine vm = new VirtualMachine(cg);
		vm.load();
		ArcThread thr = new ArcThread(vm);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(Fixnum.get(555729757), thr.getAcc());
		assertEquals(0, thr.getSP());
		assertEquals(13, thr.getIP());
	}
}

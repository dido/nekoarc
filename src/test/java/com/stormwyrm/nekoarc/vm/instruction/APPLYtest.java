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
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class APPLYtest {
	@Test
	public void testCons() {
		// cont 13; ldi 2; push; ldl 0; apply 1; ret
        byte[] inst = {(byte) 0x52, 0x0d, 0x00, 0x00, 0x00,
                0x44, 0x02, 0x00, 0x00, 0x00,
                0x01,
                0x43, 0x00, 0x00, 0x00, 0x00,
                0x4c, 0x01,
                0x0d};
        ArcObject[] literals = new ArcObject[1];
		literals[0] = new Cons(Fixnum.get(10), new Cons(Fixnum.get(11), new Cons(Fixnum.get(12), Nil.NIL)));
		VirtualMachine vm = new VirtualMachine();
		vm.load(inst, literals);
		ArcThread thr = new ArcThread(vm);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(12, ((Fixnum)thr.getAcc()).fixnum);
		assertEquals(19, thr.getIP());
		assertEquals(0, thr.getSP());
	}

	@Test
	public void testClosure() {
		// env 1 0 0; ldi 2; push; cls 0; apply 1; ret; env 1 0 0; lde0 0; push; lde 1 0; add; ret
		// more or less the code that should be produced by compiling (fn (x) ((fn (y) (+ x y)) 2))
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.ENV.emit(cg, 1, 0, 0);
		Op.LDIP.emit(cg, 2);
		Op.CLS.emit(cg, "lambda");
		Op.APPLY.emit(cg, 1);
		Op.RET.emit(cg);
		cg.endCode();
		cg.startCode();
		Op.ENV.emit(cg, 1, 0, 0);
		Op.LDE0P.emit(cg, 0);
		Op.LDE.emit(cg, 1, 0);
		Op.ADD.emit(cg);
		Op.RET.emit(cg);
		cg.literal("lambda", cg.endCode());

        VirtualMachine vm = new VirtualMachine(cg);
		ArcThread thr = new ArcThread(vm);
		thr.setAcc(Nil.NIL);
		thr.push(Fixnum.get(5));
		thr.setargc(1);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(7, ((Fixnum)thr.getAcc()).fixnum);
	}
}

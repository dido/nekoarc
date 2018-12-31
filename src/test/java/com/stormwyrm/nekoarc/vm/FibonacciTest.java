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
package com.stormwyrm.nekoarc.vm;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.Nil;
import org.junit.Test;

public class FibonacciTest {
	/* This is a recursive Fibonacci test, essentially:
	 *
	 * (afn (x) (if (is x 0) 1 (is x 1) 1 (+ (self (- x 1)) (self (- x 2)))))
	 *
	 * Since this isn't tail recursive it runs in exponential time and will use up a lot of
	 * stack space for continuations. If we don't migrate continuations to the heap this will
	 * very quickly use up stack space if it's set low enough.
	 *
	 * Also serves to test the code generator.
	 */
	@Test
	public void test() {
		CodeGen cg = new CodeGen();

		cg.startCode();
		Op.ENV.emit(cg, 1, 0, 0);
		Op.LDE0P.emit(cg, 0);
		Op.LDI.emit(cg, 0);
		Op.IS.emit(cg);
		Op.JF.emit(cg, "l1");
		Op.LDI.emit(cg, 1);
		Op.RET.emit(cg);
		cg.label("l1", Op.LDE0P.emit(cg, 0));
		Op.LDI.emit(cg, 1);
		Op.IS.emit(cg);
		Op.JF.emit(cg, "l2");
		Op.LDI.emit(cg, 1);
		Op.RET.emit(cg);
		cg.label("l2", Op.CONT.emit(cg, "l3"));
		Op.LDE0P.emit(cg, 0);
		Op.LDI.emit(cg, 1);
		Op.SUB.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDL.emit(cg, "self");
		Op.APPLY.emit(cg, 1);
		cg.label("l3", Op.PUSH.emit(cg));
		Op.CONT.emit(cg, "l4");
		Op.LDE0P.emit(cg, 0);
		Op.LDI.emit(cg, 2);
		Op.SUB.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDL.emit(cg, "self");
		Op.APPLY.emit(cg, 1);
		cg.label("l4", Op.ADD.emit(cg));
		Op.RET.emit(cg);
		Code code = cg.endCode();

		cg.literal("self", new Closure(Nil.NIL, code));

		VirtualMachine vm = new VirtualMachine(cg);
		vm.initSyms();

		ArcThread thr = new ArcThread(vm,8);

		vm.load();
		thr.setargc(1);
		thr.push(Fixnum.get(25));
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(121393, ((Fixnum)thr.getAcc()).fixnum);
	}

}

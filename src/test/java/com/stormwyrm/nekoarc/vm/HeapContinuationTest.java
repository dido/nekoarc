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
package com.stormwyrm.nekoarc.vm;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.HeapContinuation;
import com.stormwyrm.nekoarc.HeapEnv;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.*;
import org.junit.Test;

public class HeapContinuationTest {
	/** First we try to invent a HeapContinuation out of whole cloth. */
	@Test
	public void test() {
		HeapContinuation hc;

		// Prepare an environment
		HeapEnv env = new HeapEnv(3, Nil.NIL);
		env.setEnv(0, Fixnum.get(4));
		env.setEnv(1, Fixnum.get(5));
		env.setEnv(2, Fixnum.get(6));
		// CodeGen:
		// env 2 0 0; lde0 0; push; lde0 1; apply 1; ret; hlt; hlt; hlt; hlt; hlt;
		// add everything already on the stack to the value applied to the continuation, get the
		// stuff in the environment and add it too, and return the sum.
		// add; add; add; push; lde0 0; add; push; lde0 1; add; push; lde0 2; add; ret
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.ENV.emit(cg, 2, 0, 0);
		Op.LDE0P.emit(cg, 0);
		Op.LDE0.emit(cg, 1);			// continuation
		Op.APPLY.emit(cg, 1);
		Op.RET.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		Op.HLT.emit(cg);
		int contaddr = Op.ADD.emit(cg);
		Op.ADD.emit(cg);
		Op.ADD.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDE0.emit(cg, 0);
		Op.ADD.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDE0.emit(cg, 1);
		Op.ADD.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDE0.emit(cg, 2);
		Op.ADD.emit(cg);
		Op.RET.emit(cg);
		cg.endCode();

        VirtualMachine vm = new VirtualMachine(cg);
		ArcThread thr = new ArcThread(vm);

		// Synthetic HeapContinuation
		hc = new HeapContinuation(3,		// 3 stack elements
				Nil.NIL,					// previous continuation
				env,						// environment
				contaddr);						// saved IP
		// Stack elements
		hc.setIndex(0, Fixnum.get(1));
		hc.setIndex(1, Fixnum.get(2));
		hc.setIndex(2, Fixnum.get(3));

		thr.setargc(2);
		thr.push(Fixnum.get(7));
		thr.push(hc);
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(28, ((Fixnum)thr.getAcc()).fixnum);
	}

	/** Next, we make a function that recurses several times, essentially
	 *  (afn (x) (if (is x 0) 0 (+ (self (- x 1)) 2)))
	 *  If stack space is very limited on the VM, the continuations that the recursive calls need to make should
	 *  eventually migrate from the stack to the heap.
	 */
	@Test
	public void test2() {
		CodeGen cg = new CodeGen();

		cg.startCode();
		Op.ENV.emit(cg, 1, 0, 0);
		Op.LDE0P.emit(cg, 0);
		Op.LDI.emit(cg, 0);
		Op.IS.emit(cg);
		Op.JF.emit(cg, "L1");
		Op.LDI.emit(cg, 0);
		Op.RET.emit(cg);
		cg.label("L1", Op.CONT.emit(cg, "L2"));
		Op.LDE0P.emit(cg, 0);
		Op.LDI.emit(cg, 1);
		Op.SUB.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDL.emit(cg, "self");
		Op.APPLY.emit(cg, 1);
		cg.label("L2", Op.PUSH.emit(cg));
		Op.LDI.emit(cg, 2);
		Op.ADD.emit(cg);
		Op.RET.emit(cg);
		Closure clos = new Closure(Nil.NIL, cg.endCode());
		cg.literal("self", clos);

        VirtualMachine vm = new VirtualMachine(cg);
		ArcThread thr = new ArcThread(vm,5);
		thr.setargc(1);
		thr.push(Fixnum.get(100));
		thr.setAcc(clos);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(200, ((Fixnum)thr.getAcc()).fixnum);
	}

	/** Next, we make a slight variation
	 *  (afn (x) (if (is x 0) 0 (+ 2 (self (- x 1)))))
	 */
	@Test
	public void test3() {
		// env 1 0 0; lde0 0; push; ldi 0; is; jf L1; ldi 0; ret;
		// L1: ldi 2; push; cont L2; lde0 0; push; ldi 1; sub; push; ldl 0; apply 1; L2: add; ret
		CodeGen cg = new CodeGen();

		cg.startCode();
		Op.ENV.emit(cg, 1, 0, 0);
		Op.LDE0P.emit(cg, 0);
		Op.LDI.emit(cg, 0);
		Op.IS.emit(cg);
		Op.JF.emit(cg, "L1");
		Op.LDI.emit(cg, 0);
		Op.RET.emit(cg);
		cg.label("L1", Op.LDIP.emit(cg, 2));
		Op.CONT.emit(cg, "L2");
		Op.LDE0P.emit(cg, 0);
		Op.LDI.emit(cg, 1);
		Op.SUB.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDL.emit(cg, "self");
		Op.APPLY.emit(cg, 1);
		cg.label("L2", Op.ADD.emit(cg));
		Op.RET.emit(cg);
		Closure clos = new Closure(Nil.NIL, cg.endCode());
		cg.literal("self", clos);

        VirtualMachine vm = new VirtualMachine(cg);
		ArcThread thr = new ArcThread(vm,6);
        ArcObject[] literals = new ArcObject[1];
		thr.setargc(1);
		thr.push(Fixnum.get(1));
		thr.setAcc(clos);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(2, ((Fixnum)thr.getAcc()).fixnum);
	}

}

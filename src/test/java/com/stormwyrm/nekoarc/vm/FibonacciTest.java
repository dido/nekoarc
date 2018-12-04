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
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Closure;
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
		int codestart;
		CodeGen cg = new CodeGen();

		assertEquals(0x00, codestart = Op.ENV.emit(cg, 1, 0, 0));
		assertEquals(0x04, Op.LDE0.emit(cg, 0));
		assertEquals(0x06, Op.PUSH.emit(cg));
		assertEquals(0x07, Op.LDI.emit(cg, 0));
		assertEquals(0x0c, Op.IS.emit(cg));
		int setl1;
		assertEquals(0x0d, setl1 = Op.JF.emit(cg, 0));
		assertEquals(0x12, Op.LDI.emit(cg, 1));
		assertEquals(0x17, Op.RET.emit(cg));
		int l1;
		assertEquals(0x18, l1 = Op.LDE0.emit(cg, 0));
		assertEquals(6, cg.patchRelativeBranch(setl1, l1));
		assertEquals(Op.JF.opcode(), cg.getAtPos(setl1));
		assertEquals(6, cg.getAtPos(setl1+1));
		assertEquals(0, cg.getAtPos(setl1+2));
		assertEquals(0, cg.getAtPos(setl1+3));
		assertEquals(0, cg.getAtPos(setl1+4));

		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 1);
		Op.IS.emit(cg);
		int setl2 = Op.JF.emit(cg, 0);
		Op.LDI.emit(cg, 1);
		Op.RET.emit(cg);
		int l2 = Op.CONT.emit(cg, 0);
		assertEquals(6, cg.patchRelativeBranch(setl2, l2));
		Op.LDE0.emit(cg, 0);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 1);
		Op.SUB.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDL.emit(cg, 0);
		Op.APPLY.emit(cg, 1);
		int l3 = Op.PUSH.emit(cg);
		assertEquals(0x11, cg.patchRelativeBranch(l2, l3));
		int setl4 = Op.CONT.emit(cg, 0);
		Op.LDE0.emit(cg, 0);
		Op.PUSH.emit(cg);
		Op.LDI.emit(cg, 2);
		Op.SUB.emit(cg);
		Op.PUSH.emit(cg);
		Op.LDL.emit(cg, 0);
		Op.APPLY.emit(cg, 1);
		int l4 = Op.ADD.emit(cg);
		assertEquals(0x11, cg.patchRelativeBranch(setl4, l4));
		int len = Op.RET.emit(cg) + 1;

		// env 1 0 0; lde0 0; push; ldi 0; is; jf xxx; ldi 1; ret; lde0 0; push ldi 1; is; jf xxx; ldi 1; ret;
		// cont xxx; lde0 0; push; ldi 1; sub; push; ldl 0; apply 1; push;
		// cont xxx; lde0 0; push; ldi 2; sub; push; ldl 0; apply 1; add; ret
        byte[] inst = {(byte) 0xca, 0x01, 0x00, 0x00,    // env 1 0 0
                0x69, 0x00,                                // lde0 0
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x1f,                                    // is
                0x50, 0x06, 0x00, 0x00, 0x00,            // jf L1 (6)
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x0d,                                    // ret
                0x69, 0x00,                                // L1: lde0 0
                0x01,                                    // push
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x1f,                                    // is
                0x50, 0x06, 0x00, 0x00, 0x00,            // jf L2 (6)
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x0d,                                    // ret
                (byte) 0x52, 0x11, 0x00, 0x00, 0x00,        // L2: cont L3 (0x11)
                0x69, 0x00,                                // lde0 0
                0x01,                                    // push
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x16,                                    // sub
                0x01,                                    // push
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x4c, 0x01,                                // apply 1
                0x01,                                    // L3: push
                (byte) 0x52, 0x11, 0x00, 0x00, 0x00,        // cont L4 (0x11)
                0x69, 0x00,                                // lde0 0
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x16,                                    // sub
                0x01,                                    // push
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x4c, 0x01,                                // apply 1
                0x15,                                    // add
                0x0d                                    // ret
        };
		assertEquals(inst.length, len);

		for (int i=0; i<len; i++)
			assertEquals(inst[i], cg.getAtPos(i));
		cg.literal(new Closure(Nil.NIL, Fixnum.get(codestart)));

		VirtualMachine vm = new VirtualMachine(cg);
		vm.initSyms();

		ArcThread thr = new ArcThread(vm,8);

		vm.load();
		thr.setargc(1);
		thr.push(Fixnum.get(25));
		thr.setAcc(Nil.NIL);
		assertTrue(thr.runnable());
		thr.main();
		assertFalse(thr.runnable());
		assertEquals(121393, ((Fixnum)thr.getAcc()).fixnum);
	}

}

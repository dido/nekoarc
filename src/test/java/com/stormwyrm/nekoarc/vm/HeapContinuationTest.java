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

import com.stormwyrm.nekoarc.HeapContinuation;
import com.stormwyrm.nekoarc.HeapEnv;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.Closure;
import com.stormwyrm.nekoarc.types.Fixnum;
import org.junit.Test;

public class HeapContinuationTest
{
	/** First we try to invent a HeapContinuation out of whole cloth. */
	@Test
	public void test()
	{
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
        byte[] inst = {(byte) 0xca, 0x02, 0x00, 0x00,    // env 1 0 0
                0x69, 0x00,                                // lde0 0  ; value (fixnum)
                0x01,                                    // push
                0x69, 0x01,                                // lde0 1  ; continuation
                0x4c, 0x01,                                // apply 1
                0x0d,                                    // ret
                0x14,                                    // hlt
                0x14,                                    // hlt
                0x14,                                    // hlt
                0x14,                                    // hlt
                0x14,                                    // hlt
                0x14,                                    // hlt
                0x14,                                    // hlt
                0x14,                                    // hlt
                0x15,                                    // add		; continuation ip address
                0x15,                                    // add
                0x15,                                    // add
                0x01,                                    // push
                0x69, 0x00,                                // lde0 0
                0x15,                                    // add
                0x01,                                    // push
                0x69, 0x01,                                // lde0 1
                0x15,                                    // add
                0x01,                                    // push
                0x69, 0x02,                                // lde0 2
                0x15,                                    // add
                0x0d,                                    // ret
        };
        VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm,1024);
		vm.load(inst);

		// Synthetic HeapContinuation
		hc = new HeapContinuation(3,		// 3 stack elements
				Nil.NIL,					// previous continuation
				env,						// environment
				20,
				thr.here);						// saved IP
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
		// env 1 0 0; lde0 0; push; ldi 0; is; jf L1; ldi 2; ret;
		// L1: cont L2; lde0 0; push; ldi 1; sub; push; ldl 0; apply 1; L2: push; ldi 2; add; ret
        byte[] inst = {(byte) 0xca, 0x01, 0x00, 0x00,    // env 1 0 0
                0x69, 0x00,                                // lde0 0
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x1f,                                    // is
                0x50, 0x06, 0x00, 0x00, 0x00,            // jf L1 (6)
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x0d,                                    // ret
                (byte) 0x52, 0x11, 0x00, 0x00, 0x00,        // L1: cont L2 (0x11)
                0x69, 0x00,                                // lde0 0
                0x01,                                    // push
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x16,                                    // sub
                0x01,                                    // push
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x4c, 0x01,                                // apply 1
                0x01,                                    // L2: push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x15,                                    // add
                0x0d                                    // ret
        };

        VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm,4);
        ArcObject[] literals = new ArcObject[1];
		literals[0] = new Closure(Nil.NIL, Fixnum.get(0));
		vm.load(inst, literals);
		thr.setargc(1);
		thr.push(Fixnum.get(100));
		thr.setAcc(literals[0]);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(200, ((Fixnum)thr.getAcc()).fixnum);
	}

	/** Next, we make a slight variation
	 *  (afn (x) (if (is x 0) 0 (+ 2 (self (- x 1)))))
	 */
	@Test
	public void test3()
	{
		// env 1 0 0; lde0 0; push; ldi 0; is; jf L1; ldi 2; ret;
		// L1: ldi 2; push; cont L2; lde0 0; push; ldi 1; sub; push; ldl 0; apply 1; L2: add; ret
        byte[] inst = {(byte) 0xca, 0x01, 0x00, 0x00,    // env 1 0 0
                0x69, 0x00,                                // lde0 0
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x1f,                                    // is
                0x50, 0x06, 0x00, 0x00, 0x00,            // jf L1 (6)
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x0d,                                    // ret
                0x44, 0x02, 0x00, 0x00, 0x00,            // L1: ldi 2
                0x01,                                    // push
                (byte) 0x52, 0x11, 0x00, 0x00, 0x00,        // cont L2 (0x11)
                0x69, 0x00,                                // lde0 0
                0x01,                                    // push
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x16,                                    // sub
                0x01,                                    // push
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x4c, 0x01,                                // apply 1
                0x15,                                    // L2: add
                0x0d                                    // ret
        };
        VirtualMachine vm = new VirtualMachine();
		ArcThread thr = new ArcThread(vm,5);
        ArcObject[] literals = new ArcObject[1];
		literals[0] = new Closure(Nil.NIL, Fixnum.get(0));
		vm.load(inst, literals);
		thr.setargc(1);
		thr.push(Fixnum.get(1));
		thr.setAcc(literals[0]);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(2, ((Fixnum)thr.getAcc()).fixnum);
	}

}

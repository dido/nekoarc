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

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.*;
import org.junit.Test;

/** Test for Java to Arc bytecode calls */
public class Java2ArcTest {

	@Test
	public void test() {
		Builtin builtin = new Builtin("test", 1, 0, 0, false) {
			@Override
			public ArcObject invoke(InvokeThread ithr) throws Throwable {
				ArcObject arg = ithr.getenv(0, 0);
				return(ithr.apply(arg, Fixnum.get(1)));
			}
		};
		// Apply the above builtin
		// env 0 0 0; ldl 0; push; ldl 1; apply 1; ret;
		// to (fn (x) (+ x 1))
		// env 1 0 0; lde0 0; push; ldi 1; add; ret
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.ENV.emit(cg, 0, 0, 0);
		Op.LDLP.emit(cg, "lambda");
		Op.LDL.emit(cg, "builtin");
		Op.APPLY.emit(cg, 1);
		Op.RET.emit(cg);
		cg.endCode();
		cg.startCode();
		Op.ENV.emit(cg, 1, 0, 0);
		Op.LDE0P.emit(cg, 0);
		Op.LDI.emit(cg, 1);
		Op.ADD.emit(cg);
		Op.RET.emit(cg);
		Code lambda = cg.endCode();
		cg.literal("lambda", new Closure(Nil.NIL, lambda));
		cg.literal("builtin", builtin);
        VirtualMachine vm = new VirtualMachine(cg);
        ArcThread thr = new ArcThread(vm, 1024);
        thr.setargc(0);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(2, ((Fixnum)thr.getAcc()).fixnum);
	}

}

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

import com.stormwyrm.nekoarc.HeapEnv;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.Closure;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class CLStest {
	@Test
	public void test() {
		// env 1 0 0 cls 2; ret; env 0 0 0; ldi 1; ret
		CodeGen cg = new CodeGen();
		cg.startCode();
		Op.ENV.emit(cg, 1, 0, 0);
		Op.CLS.emit(cg, "lambda");
		Op.RET.emit(cg);
		cg.endCode();
		cg.startCode();
		Op.ENV.emit(cg, 0, 0, 0);
		Op.LDI.emit(cg, 1);
		Op.RET.emit(cg);
		cg.literal("lambda", cg.endCode());
		VirtualMachine vm = new VirtualMachine(cg);
		ArcThread thr = new ArcThread(vm);
		thr.setAcc(Nil.NIL);
		thr.setargc(1);
		thr.push(Nil.NIL);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertTrue(thr.getAcc() instanceof Closure);
		// assertTrue(((Closure)thr.getAcc()).env instanceof HeapEnv);
		// assertEquals(12, ((Closure)thr.getAcc()).ip);
	}

}

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

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.Closure;
import com.stormwyrm.nekoarc.types.Fixnum;
import org.junit.Test;

/** Test for Java to Arc bytecode calls */
public class Java2ArcTest 
{

	@Test
	public void test()
	{
		Builtin builtin = new Builtin("test", 1, 0, 0, false)
		{
			@Override
			public ArcObject invoke(InvokeThread ithr)
			{
				ArcObject arg = ithr.getenv(0, 0);
				return(ithr.apply(arg, Fixnum.get(1)));
			}
		};
		// Apply the above builtin
		// env 0 0 0; ldl 0; push; ldl 1; apply 1; ret;
		// to (fn (x) (+ x 1))
		// env 1 0 0; lde0 0; push; ldi 1; add; ret
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x00, 0x00, 0x00, 0x00,            // ldl 0
                0x01,                                    // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 0
                0x4c, 0x01,                                // apply 1
                0x0d,                                    // ret
                (byte) 0xca, 0x01, 0x00, 0x00,            // env 1 0 0
                0x69, 0x00,                                // lde0 0
                0x01,                                    // push
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x15,                                    // add
                0x0d                                    // ret
        };

        ArcObject[] literals = new ArcObject[2];
        literals[0] = new Closure(Nil.NIL, Fixnum.get(18));	// position of second
        literals[1] = builtin;
        VirtualMachine vm = new VirtualMachine();
        ArcThread thr = new ArcThread(vm, 1024);

        vm.load(inst, literals);
        thr.setargc(0);
		assertTrue(thr.runnable());
		thr.run();
		assertFalse(thr.runnable());
		assertEquals(2, ((Fixnum)thr.getAcc()).fixnum);
	}

}

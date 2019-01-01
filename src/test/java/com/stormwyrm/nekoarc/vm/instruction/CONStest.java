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
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class CONStest extends TestTemplate {

	@Test
	public void test() {
		// ldi 2; push; ldi 1; cons; hlt
        doTestWithByteCode(new byte[]{0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x19,
                0x14},
				(cg) ->{
        	cg.startCode();
        	Op.LDI.emit(cg, 2);
        	Op.PUSH.emit(cg);
        	Op.LDI.emit(cg, 1);
        	Op.CONS.emit(cg);
        	Op.HLT.emit(cg);
        	cg.endCode();
        	return(cg);
				},
				(t)->{
        	assertTrue(t.getAcc() instanceof Cons);
			assertEquals(2, ((Fixnum)t.getAcc().car()).fixnum);
			assertEquals(1, ((Fixnum)t.getAcc().cdr()).fixnum);
				});
	}

}

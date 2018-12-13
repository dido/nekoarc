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

import com.stormwyrm.nekoarc.*;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Flonum;
import org.junit.Test;

import static org.junit.Assert.*;

public class MULtest extends TestTemplate {
    @Test
    public void testFixnumTimesFixnum() {
        MakeCode mc = (cg) -> {
            Op.LDIP.emit(cg, 6);
            Op.LDI.emit(cg, 7);
            Op.MUL.emit(cg);
            Op.HLT.emit(cg);
            return(cg);
        };
        doTest(mc, Fixnum.get(42));
    }

    @Test
    public void testFixnumTimesFlonum() {
        MakeCode mc = (cg) -> {
          Op.LDLP.emit(cg, "pi");
          Op.LDI.emit(cg, 4);
          Op.MUL.emit(cg);
          Op.HLT.emit(cg);
          cg.literal("pi", new Flonum(3.1415926535));
          return(cg);
        };
        doTest(mc, (t)-> assertEquals(12.566370614, ((Flonum)t.getAcc()).flonum, 1e-6));
    }

    @Test
    public void testFlonumTimesFixnum() {
        MakeCode mc = (cg) -> {
            Op.LDIP.emit(cg, 4);
            Op.LDL.emit(cg, "pi");
            Op.MUL.emit(cg);
            Op.HLT.emit(cg);
            cg.literal("pi", new Flonum(3.1415926535));
            return(cg);
        };
        doTest(mc, (t)-> assertEquals(12.566370614, ((Flonum)t.getAcc()).flonum, 1e-6));
    }

    @Test
    public void testFlonumTimesFlonum() {
        MakeCode mc = (cg) -> {
            Op.LDLP.emit(cg, "e");
            Op.LDL.emit(cg, "pi");
            Op.MUL.emit(cg);
            Op.HLT.emit(cg);
            cg.literal("pi", new Flonum(3.1415926535));
            cg.literal("e", new Flonum(2.7182818285));
            return(cg);
        };
        doTest(mc, (t)-> assertEquals(8.539734222558147, ((Flonum)t.getAcc()).flonum, 1e-6));
    }
}
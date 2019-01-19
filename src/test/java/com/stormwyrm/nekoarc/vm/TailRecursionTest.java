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

import com.stormwyrm.nekoarc.MakeCode;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.types.Fixnum;
import org.junit.Test;

public class TailRecursionTest extends TestTemplate {
    /*
     * This is a simple test for tail recursion. Essentially
     * (fn (x y t) (+ ((afn (acc z) (if (is z 0) acc (self (+ acc x) (- z 1)))) 0 y) t))
     * For large enough values of y the stack would overflow with garbage environments
     * unless we do tail recursion properly.
     */
    @Test
    public void test() {
        // Reduce thread stack size to really small so that if we don't do tail recursion optimization properly
        // we will overflow the stack.
        stackSize = 14;
        // (fn (x y t) (+ ...)
        // env 3 1 0; cont 19; ldi 0; push; lde0 1; push; cls 7; ste0 3; apply 2; push; lde0 2; add; ret;
        // (note that there are no cont instructions in the second function because all calls are tail calls)
        // (afn (acc z) (if (is z 0) acc (self (+ acc x) (- z 1)))) 0 y) t)
        // env 2 0 0; lde0 1; push; ldi 0; is; jf xxx; lde0 0; ret; lde0 0; push; lde 1 0; add; push; lde0 1;
        // push; ldi 1; sub; push; lde 1 2; apply 2; ret

        MakeCode mc = (cg) -> {
            cg.startCode();
            Op.ENV.emit(cg, 3, 1, 0);
            Op.CONT.emit(cg, "LBL0");
            Op.LDI.emit(cg, 0);
            Op.PUSH.emit(cg);
            Op.LDE0.emit(cg, 1);
            Op.PUSH.emit(cg);
            Op.CLS.emit(cg, "LAMBDA");
            Op.STE0.emit(cg, 3);
            Op.APPLY.emit(cg, 2);
            cg.label("LBL0", Op.PUSH.emit(cg));
            Op.LDE0.emit(cg, 2);
            Op.ADD.emit(cg);
            Op.RET.emit(cg);
            cg.endCode();
            cg.startCode();
            Op.ENV.emit(cg, 2, 0, 0);
            Op.LDE0.emit(cg, 1);
            Op.PUSH.emit(cg);
            Op.LDI.emit(cg, 0);
            Op.IS.emit(cg);
            Op.JF.emit(cg, "LBL1");
            Op.LDE0.emit(cg, 0);
            Op.RET.emit(cg);
            cg.label("LBL1", Op.LDE0.emit(cg, 0));
            Op.PUSH.emit(cg);
            Op.LDE.emit(cg, 1, 0);
            Op.ADD.emit(cg);
            Op.PUSH.emit(cg);
            Op.LDE0.emit(cg, 1);
            Op.PUSH.emit(cg);
            Op.LDI.emit(cg, 1);
            Op.SUB.emit(cg);
            Op.PUSH.emit(cg);
            Op.LDE.emit(cg, 1, 3);
            Op.APPLY.emit(cg, 2);
            Op.RET.emit(cg);
            cg.literal("LAMBDA", cg.endCode());
            return (cg);
        };

        doTest(mc, (t) -> {
                    t.setargc(3);
                    t.push(Fixnum.get(1));
                    t.push(Fixnum.get(50000));
                    t.push(Fixnum.get(1));
                    t.setAcc(Nil.NIL);
                },
                (t) -> assertEquals(50001, ((Fixnum) t.getAcc()).fixnum));
    }

}

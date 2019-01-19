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
package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class CCCTest extends TestTemplate {
    @Test
    public void testCCCsimple() {
        doTest((cg) -> {
            cg.startCode();
            Op.ENV.emit(cg, 0, 0, 0);
            Op.CONT.emit(cg, "L1");
            Op.CLS.emit(cg, "lambda");
            Op.PUSH.emit(cg);
            Op.LDG.emit(cg, "ccc");
            Op.APPLY.emit(cg, 1);
            cg.label("L1", Op.PUSH.emit(cg));
            Op.LDI.emit(cg, 1);
            Op.ADD.emit(cg);
            Op.RET.emit(cg);
            cg.endCode();

            cg.startCode();
            Op.ENV.emit(cg, 1, 0, 0);
            Op.LDIP.emit(cg, 41);
            Op.LDE0.emit(cg, 0);
            Op.APPLY.emit(cg, 1);
            Op.LDI.emit(cg, 21);
            Op.RET.emit(cg);
            Code lambda = cg.endCode();
            cg.literal("ccc", Symbol.intern("ccc"));
            cg.literal("lambda", lambda);
            return(cg); }, Fixnum.get(42));
    }

    @Test
    public void testCCCTail() {
        CodeGen cg = new CodeGen();
        // This is basically also
        // (ccc [(_42) 21])
        // but there is no continuation, as would be generated with ccc in a tail position

        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.CLS.emit(cg, "func");         // function [(_ 42) 21]
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, "ccc");         // symbol ccc
        Op.APPLY.emit(cg, 1);
        Code c = cg.endCode();

        // [(_ 42) 21]
        cg.startCode();
        Op.ENV.emit(cg, 1, 0, 0);
        Op.LDIP.emit(cg, 42);
        Op.LDE0.emit(cg, 0);
        Op.APPLY.emit(cg, 1);
        Op.LDI.emit(cg, 21);
        Op.RET.emit(cg);
        Code func = cg.endCode();

        cg.literal("ccc", Symbol.intern("ccc"));
        cg.literal("func", func);

        VirtualMachine vm = new VirtualMachine(cg);
        ArcThread thr = new ArcThread(vm);
        vm.initSyms();

        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(42, ((Fixnum)thr.getAcc()).fixnum);
    }
}
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

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicWindTest {
    @Test
    public void testDWsimple() {
        // This is a very simple test that basically does
        // (dynamic-wind (fn () (assign before (+ 1 before))
        //               (fn () (assign during (+ 2 before))
        //               (fn () (assign after (+ 3 before)))
        CodeGen cg = new CodeGen();
        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.CONT.emit(cg, "contdest");
        Op.CLS.emit(cg, "fbefore");         // before
        Op.PUSH.emit(cg);
        Op.CLS.emit(cg, "fduring");         // during
        Op.PUSH.emit(cg);
        Op.CLS.emit(cg, "fafter");         // after
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, "dynamic-wind");         // dynamic-wind
        Op.APPLY.emit(cg, 3);
        cg.label("contdest", Op.RET.emit(cg));
        cg.endCode();

        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 1);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, "before");
        Op.ADD.emit(cg);
        Op.STG.emit(cg, "before");
        Op.RET.emit(cg);
        Code fbefore = cg.endCode();

        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 2);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, "during");
        Op.ADD.emit(cg);
        Op.STG.emit(cg, "during");
        Op.RET.emit(cg);
        Code fduring = cg.endCode();

        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 3);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, "after");
        Op.ADD.emit(cg);
        Op.STG.emit(cg, "after");
        Op.RET.emit(cg);
        Code fafter = cg.endCode();

        cg.literal("dynamic-wind", Symbol.intern("dynamic-wind"));
        cg.literal("fbefore", fbefore);
        cg.literal("fduring", fduring);
        cg.literal("fafter", fafter);
        cg.literal("before", Symbol.intern("before"));
        cg.literal("during", Symbol.intern("during"));
        cg.literal("after", Symbol.intern("after"));
        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();
        vm.bind((Symbol) Symbol.intern("before"), Fixnum.get(1));
        vm.bind((Symbol) Symbol.intern("during"), Fixnum.get(2));
        vm.bind((Symbol) Symbol.intern("after"), Fixnum.get(3));

        ArcThread thr = new ArcThread(vm);
        thr.setIP(0);
        thr.run();
        ArcObject retval = thr.getAcc();
        assertEquals(4, ((Fixnum)retval).fixnum);
        assertEquals(2, ((Fixnum)vm.value((Symbol) Symbol.intern("before"))).fixnum);
        assertEquals(4, ((Fixnum)vm.value((Symbol) Symbol.intern("during"))).fixnum);
        assertEquals(6, ((Fixnum)vm.value((Symbol) Symbol.intern("after"))).fixnum);
    }

    @Test
    public void testDWCCC() {
        // dynamic-wind and call/cc interaction
        // Essentially
        // (+ (ccc (fn (cont) (dynamic-wind (fn () (assign before 1))
        //                                  (fn () (assign during 2) (cont during))
        //                                  (fn () (assign after 3))))) before during after)
        // This should of course yield 8, and before, during, and after must have the values 1, 2, and 3
        // respectively

        CodeGen cg = new CodeGen();

        int start = cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.CONT.emit(cg, "contdest");
        Op.CLS.emit(cg, "cccarg");
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, "ccc");                     // ccc
        Op.APPLY.emit(cg, 1);
        cg.label("contdest", Op.PUSH.emit(cg));
        Op.LDG.emit(cg, "before");                     // before
        Op.ADD.emit(cg);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 3);                     // during
        Op.ADD.emit(cg);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 4);                     // after
        Op.ADD.emit(cg);
        Op.RET.emit(cg);
        cg.endCode();

        cg.startCode();
        Op.ENV.emit(cg, 1, 0, 0);
        Op.CONT.emit(cg, "contdest2");
        Op.CLS.emit(cg, "fbefore");     // before
        Op.PUSH.emit(cg);
        Op.CLS.emit(cg, "fduring");     // during
        Op.PUSH.emit(cg);
        Op.CLS.emit(cg, "fafter");       // after
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, "dynamic-wind");                  // dynamic-wind
        Op.APPLY.emit(cg, 3);
        cg.label("contdest2", Op.RET.emit(cg));
        Code cccarg = cg.endCode();

        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 1);
        Op.STG.emit(cg, "before");                 // before
        Op.RET.emit(cg);
        Code fbefore = cg.endCode();

        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 2);
        Op.STG.emit(cg, "during");                 // during
        Op.LDG.emit(cg, "during");
        Op.PUSH.emit(cg);
        Op.LDE.emit(cg, 1, 0);         // continuation passed
        Op.APPLY.emit(cg, 1);
        Op.RET.emit(cg);
        Code fduring = cg.endCode();

        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 3);
        Op.STG.emit(cg, "after");             // after
        Op.RET.emit(cg);
        Code fafter = cg.endCode();

        cg.literal("ccc", Symbol.intern("ccc"));
        cg.literal("dynamic-wind", Symbol.intern("dynamic-wind"));
        cg.literal("before", Symbol.intern("before"));
        cg.literal("during", Symbol.intern("during"));
        cg.literal("after", Symbol.intern("after"));
        cg.literal("cccarg", cccarg);
        cg.literal("fbefore", fbefore);
        cg.literal("fduring", fduring);
        cg.literal("fafter", fafter);

        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();

        ArcThread thr = new ArcThread(vm);
        thr.setIP(start);
        thr.run();
        ArcObject retval = thr.getAcc();
        assertEquals(8, ((Fixnum)retval).fixnum);
        assertEquals(1, ((Fixnum)vm.value((Symbol) Symbol.intern("before"))).fixnum);
        assertEquals(2, ((Fixnum)vm.value((Symbol) Symbol.intern("during"))).fixnum);
        assertEquals(3, ((Fixnum)vm.value((Symbol) Symbol.intern("after"))).fixnum);
    }
}
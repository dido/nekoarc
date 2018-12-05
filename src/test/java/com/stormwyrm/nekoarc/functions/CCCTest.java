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
package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class CCCTest {
    @Test
    public void testCCCsimple() {
        CodeGen cg = new CodeGen();
        // This is basically
        // (ccc (fn (esc) (esc 42) 21))

        Op.ENV.emit(cg, 0, 0, 0);
        int contpos = Op.CONT.emit(cg, 0);
        Op.LDL.emit(cg, 1);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 0);
        Op.APPLY.emit(cg, 1);
        int contdest = Op.RET.emit(cg);
        cg.patchRelativeBranch(contpos, contdest);

        // (fn (esc) (esc 42) 21)
        int func = Op.ENV.emit(cg, 1, 0, 0);
        Op.LDI.emit(cg, 42);
        Op.PUSH.emit(cg);
        Op.LDE0.emit(cg, 0);
        Op.APPLY.emit(cg, 1);
        Op.LDI.emit(cg, 21);
        Op.RET.emit(cg);
        cg.literal(Symbol.intern("ccc"));
        cg.literal(new Closure(Nil.NIL, Fixnum.get(func)));

        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();

        ArcThread thr = vm.spawn(0);
        ArcObject retval = thr.join();
        assertEquals(42, ((Fixnum)retval).fixnum);
    }

    @Test
    public void testCCCTail() {
        CodeGen cg = new CodeGen();
        // This is basically also
        // (ccc [(_42) 21])
        // but there is no continuation, as would be generated with ccc in a tail position

        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDL.emit(cg, 1);         // function [(_ 42) 21]
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 0);         // symbol ccc
        Op.APPLY.emit(cg, 1);

        // [(_ 42) 21]
        int func = Op.ENV.emit(cg, 1, 0, 0);
        Op.LDI.emit(cg, 42);
        Op.PUSH.emit(cg);
        Op.LDE0.emit(cg, 0);
        Op.APPLY.emit(cg, 1);
        Op.LDI.emit(cg, 21);
        Op.RET.emit(cg);

        cg.literal(Symbol.intern("ccc"));
        cg.literal(new Closure(Nil.NIL, Fixnum.get(func)));

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
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

package com.stormwyrm.nekoarc.functions.arith;

import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddTest {
    @Test
    public void test0() {
        CodeGen cg = new CodeGen();
        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDG.emit(cg, "+");
        Op.APPLY.emit(cg, 0);
        Op.RET.emit(cg);
        cg.endCode();
        cg.literal("+", Symbol.intern("+"));
        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();
        vm.load();
        ArcThread thr = new ArcThread(vm);
        thr.vm.initSyms();

        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(0, ((Fixnum)thr.getAcc()).fixnum);
    }

    private void testtmpl(byte[] inst, int expected) {
        VirtualMachine vm = new VirtualMachine();
        vm.load(inst, new ArcObject[]{Symbol.intern("+")});
        vm.initSyms();
        ArcThread thr = new ArcThread(vm);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(expected, ((Fixnum)thr.getAcc()).fixnum);
    }

    @Test
    public void test1() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };

        testtmpl(inst, 1);
    }

    @Test
    public void test2() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        testtmpl(inst, 3);
    }

    @Test
    public void test3() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x44, 0x03, 0x00, 0x00, 0x00,            // ldi 3
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x03,                                // apply 3
                0x0d                                    // ret
        };
        testtmpl(inst, 6);
    }
}
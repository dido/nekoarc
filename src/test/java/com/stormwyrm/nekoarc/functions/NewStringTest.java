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

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Rune;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewStringTest {
    @Test
    public void test1() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,   // env 0 0 0
                0x44, 0x05, 0x00, 0x00, 0x00,            // ldi 5
                0x01,                                   // push
                0x45, 0x00, 0x00, 0x00, 0x00,           // ldg 0
                0x4c, 0x01,                             // apply 1
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern("newstring");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertEquals("\u0000\u0000\u0000\u0000\u0000", thr.getAcc().toString());
    }

    @Test
    public void test2() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,   // env 0 0 0
                0x44, 0x05, 0x00, 0x00, 0x00,            // ldi 5
                0x01,                                   // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                   // push
                0x45, 0x00, 0x00, 0x00, 0x00,           // ldg 0
                0x4c, 0x02,                             // apply 1
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern("newstring");
        literals[1] = Rune.get(0x41);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertEquals("AAAAA", thr.getAcc().toString());
    }
}
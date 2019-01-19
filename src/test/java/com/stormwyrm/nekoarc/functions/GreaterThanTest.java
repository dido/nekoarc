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
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Flonum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class GreaterThanTest {
    @Test
    public void test0() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x00,                                // apply 0
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test1Fixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test1Flonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x01,                                // apply 1
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(3.14159);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test2aFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(Nil.NIL, thr.getAcc());
    }

    @Test
    public void test2bFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test2aFixnumFlonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(1e100);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(Nil.NIL, thr.getAcc());
    }

    @Test
    public void test2bFixnumFlonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[2];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(0.31831);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test2aFlonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldl 1
                0x01,                                    // push
                0x43, 0x02, 0x00, 0x00, 0x00,            // ldl 2
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[3];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(1e100);
        literals[2] = new Flonum(1e101);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(Nil.NIL, thr.getAcc());
    }

    @Test
    public void test2bFlonum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x43, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x43, 0x02, 0x00, 0x00, 0x00,            // ldl 2
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 2
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[3];
        literals[0] = Symbol.intern(">");
        literals[1] = new Flonum(1e100);
        literals[2] = new Flonum(1e-100);
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test3aFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x44, 0x03, 0x00, 0x00, 0x00,            // ldi 3
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 3
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(Nil.NIL, thr.getAcc());
    }

    @Test
    public void test3bFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x02, 0x00, 0x00, 0x00,            // ldi 2
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 3
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

    @Test
    public void test3cFixnum() {
        byte[] inst = {(byte) 0xca, 0x00, 0x00, 0x00,    // env 0 0 0
                0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
                0x01,                                    // push
                0x44, 0x03, 0x00, 0x00, 0x00,            // ldi 3
                0x01,                                    // push
                0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
                0x01,                                    // push
                0x45, 0x00, 0x00, 0x00, 0x00,            // ldg 0
                0x4c, 0x02,                                // apply 3
                0x0d                                    // ret
        };
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        ArcObject[] literals = new ArcObject[1];
        literals[0] = Symbol.intern(">");
        thr.load(inst, literals);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(True.T, thr.getAcc());
    }

}
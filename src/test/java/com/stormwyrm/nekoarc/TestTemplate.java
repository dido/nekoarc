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
package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

import static org.junit.Assert.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Templates for common types of tests
 */
public class TestTemplate {
    private ArcThread executeTest(CodeGen cg) {
        VirtualMachine vm = new VirtualMachine(cg);
        vm.load();
        ArcThread t = new ArcThread(vm);
        t.setAcc(Fixnum.get(1234));
        assertTrue(t.runnable());
        t.run();
        assertFalse(t.runnable());
        return(t);
    }

    private CodeGen codeGen(MakeCode mc) {
        return(mc.makeCode(new CodeGen()));
    }

    private CodeGen testByteCode(byte[] inst, CodeGen cg) {
        assertEquals(inst.length, cg.pos());
        for (int i=0; i<inst.length; i++)
            assertEquals(inst[i], cg.getAtPos(i));
        return(cg);
    }

    private void asserts(ArcThread t, AssertTest at) {
        at.doAssert(t);
    }

    protected void doTest(MakeCode mc, AssertTest at) {
        asserts(executeTest(codeGen(mc)), at);
    }

    protected void doTestWithByteCode(byte[] bytecode, MakeCode mc, AssertTest at) {
        asserts(executeTest(testByteCode(bytecode, codeGen(mc))), at);
    }

    /**
     * Do a simple test for equality with no bytecode
     * @param mc MakeCode lambda
     * @param expected Expected value
     */
    protected void doTest(MakeCode mc, ArcObject expected) {
        doTest(mc, (t)-> assertEquals(t.getAcc(), expected));

    }

    protected void doTestWithByteCode(byte[] bytecode, MakeCode mc, ArcObject expected) {
        doTestWithByteCode(bytecode, mc, (t)-> assertEquals(t.getAcc(), expected));
    }
}

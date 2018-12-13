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
import com.stormwyrm.nekoarc.vm.VirtualMachine;

import static org.junit.Assert.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Templates for common types of tests
 */
public class TestTemplate {
    protected int stackSize = 1024;

    /**
     * Execute test code in cg
     * @param cg The CodeGen with the code to execute
     * @return The thread which ran the code, after termination
     */
    private ArcThread executeTest(CodeGen cg) {
        return(executeTest(cg, (t)-> {}));
    }

    /**
     * Execute test code in cg
     * @param cg The CodeGen with the code to execute
     * @param tl Thread lambda to prepare the thread
     * @return The thread which ran the code after termination
     */
    private ArcThread executeTest(CodeGen cg, ThreadLambda tl) {
        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();
        vm.load();
        ArcThread t = new ArcThread(vm, stackSize);
        tl.apply(t);
        assertTrue(t.runnable());
        t.run();
        assertFalse(t.runnable());
        return(t);
    }

    /**
     * Apply a MakeCode lambda to a new CodeGen
     * @param mc MakeCode lambda to apply
     * @return a new CodeGen object to which mc has been applied
     */
    private CodeGen codeGen(MakeCode mc) {
        return(mc.makeCode(new CodeGen()));
    }

    /**
     * Compare bytecode against a codegen. Assertions for equality will be performed against it.
     * @param inst Instructions to compare against a CodeGen
     * @param cg The completed CodeGen with instructions to compare
     * @return the CodeGen that was tested
     */
    private CodeGen testByteCode(byte[] inst, CodeGen cg) {
        assertEquals(inst.length, cg.pos());
        for (int i=0; i<inst.length; i++)
            assertEquals("Code is different from ref at offset " + i, inst[i], cg.getAtPos(i));
        return(cg);
    }

    /**
     * Apply AssertTest lambda to a thread
     * @param t Thread to test
     * @param at AssertTest lambda with tests
     */
    private void asserts(ArcThread t, ThreadLambda at) {
        at.apply(t);
    }


    /**
     * Execute a test with no bytecode and provided prep and post (assertions) for run thread
     * @param mc MakeCode lambda that generates code for testing
     * @param pre Preparatory ThreadLambda to prepare thread for execution
     * @param post Post-test lambda assertions, giving terminated thread for inspection
     */
    protected void doTest(MakeCode mc, ThreadLambda pre, ThreadLambda post) {
        asserts(executeTest(codeGen(mc), pre), post);
    }

    /**
     * Execute a test with no bytecode and provided prep and post (assertions) for run thread
     * @param bytecode Reference bytecode to compare
     * @param mc MakeCode lambda that generates code for testing, output will be compared with bytecode
     * @param pre Preparatory ThreadLambda to prepare thread for execution
     * @param post Post-test lambda assertions, giving terminated thread for inspection
     */
    protected void doTestWithByteCode(byte[] bytecode, MakeCode mc, ThreadLambda pre, ThreadLambda post) {
        asserts(executeTest(testByteCode(bytecode, codeGen(mc)), pre), post);
    }

    /**
     * Execute a test with no bytecode and the provided assertion(s)
     * @param mc MakeCode lambda that generates code for testing
     * @param at AssertTest lambda for assertions, giving the terminated thread for inspection
     */
    protected void doTest(MakeCode mc, ThreadLambda at) {
        asserts(executeTest(codeGen(mc)), at);
    }

    /**
     * Execute a test with bytecode and assertions
     * @param bytecode The bytecode reference
     * @param mc MakeCode lambda that generates code for testing, will be compared with bytecode
     * @param at AssertTest lambda for assertions, giving the terminated thread for inspection by the tests
     */
    protected void doTestWithByteCode(byte[] bytecode, MakeCode mc, ThreadLambda at) {
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

    /**
     * Do a simple test for equality with bytecode
     * @param bytecode The bytecode to test against
     * @param mc MakeCode lambda that should generate code equal to bytecode
     * @param expected Expected value
     */
    protected void doTestWithByteCode(byte[] bytecode, MakeCode mc, ArcObject expected) {
        doTestWithByteCode(bytecode, mc, (t)-> assertEquals(t.getAcc(), expected));
    }
}

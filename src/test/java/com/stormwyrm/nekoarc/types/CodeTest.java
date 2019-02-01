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

package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.ciel.Ciel;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeTest {

    @Test
    public void testMarshal() {
        CodeGen cg = new CodeGen();

        cg.startCode();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDLP.emit(cg, "foo");
        Op.LDL.emit(cg, "bar");
        Op.ADD.emit(cg);
        Op.RET.emit(cg);
        cg.literal("foo", new AString("foo"));
        cg.literal("bar", new AString("bar"));
        Code code = cg.endCode();
        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();

        ArcThread thr = new ArcThread(vm);

        vm.load();
        thr.setargc(0);
        thr.setAcc(Nil.NIL);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals("foobar", thr.getAcc().toString());

        OutString os = new OutString();
        code.marshal(os);
        byte[] b = os.insideBytes();
        InString is = new InString(b, "");
        Ciel ciel = new Ciel(is);
        ciel.load();
        Code newCode = (Code) ciel.pop();
        vm = new VirtualMachine(ciel.getCG());
        vm.initSyms();
        thr = new ArcThread(vm);
        vm.load();
        thr.setargc(0);
        thr.setAcc(Nil.NIL);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals("foobar", thr.getAcc().toString());

    }

}
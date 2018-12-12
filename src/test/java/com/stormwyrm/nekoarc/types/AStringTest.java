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
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class AStringTest {
    @Test
    public void testApply() {
        CodeGen cg = new CodeGen();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDIP.emit(cg, 1);
        Op.LDL.emit(cg, 0);
        Op.APPLY.emit(cg, 1);
        Op.RET.emit(cg);
        cg.literal(new AString("日本語"));

        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();
        vm.load();
        ArcThread thr = new ArcThread(vm);

        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(0x672c, ((Rune)thr.getAcc()).rune);
        assertEquals("#\\本", thr.getAcc().toString());
    }

    @Test
    public void testUnicodeApply() {
        CodeGen cg = new CodeGen();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDIP.emit(cg, 0);
        Op.LDL.emit(cg, 0);
        Op.APPLY.emit(cg, 1);
        Op.RET.emit(cg);
        cg.literal(new AString("\uD83D\uDE1D\uD83D\uDE0E"));

        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();
        vm.load();
        ArcThread thr = new ArcThread(vm);
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(0x1f61d, ((Rune)thr.getAcc()).rune);
        assertEquals("#\\\uD83D\uDE1D", thr.getAcc().toString());

    }

    @Test
    public void testSref() {
        AString str = new AString("蛟竜");
        ArcObject obj = str.sref(Rune.get(0x9f8d), Fixnum.get(1));
        assertEquals("蛟龍", str.toString());
        assertTrue(obj.is(Rune.get(0x9f8d)));
    }

    @Test
    public void testCoerce() {
        AString s = new AString("473");
        ArcObject result;

        result = s.coerce(Symbol.intern("string"), Nil.NIL);
        assertEquals("string", result.type().toString());
        assertEquals(s, result);

        result = s.coerce(Symbol.intern("fixnum"), Nil.NIL);
        assertEquals("fixnum", result.type().toString());
        assertEquals(Fixnum.get(473), result);

        s = new AString("Kona");
        result = s.coerce(Symbol.intern("fixnum"), new Cons(Fixnum.get(27), Nil.NIL));
        assertEquals("fixnum", result.type().toString());
        assertEquals(Fixnum.get(411787), result);

        s = new AString("0.57721566490153286");
        result = s.coerce(Symbol.intern("flonum"), Nil.NIL);
        assertEquals("flonum", result.type().toString());
        assertEquals(0.57721566490153286, ((Flonum)result).flonum, 1e-12);

        s = new AString("6.02214086e23");
        result = s.coerce(Symbol.intern("flonum"), Nil.NIL);
        assertEquals("flonum", result.type().toString());
        assertEquals(6.02214086e23, ((Flonum)result).flonum, 1e-12);

        s = new AString("6.62607004e-34");
        result = s.coerce(Symbol.intern("flonum"), Nil.NIL);
        assertEquals("flonum", result.type().toString());
        assertEquals(6.62607004e-34, ((Flonum)result).flonum, 1e-12);

        s = new AString("foo");
        result = s.coerce(Symbol.intern("sym"), Nil.NIL);
        assertEquals("sym", result.type().toString());
        assertEquals(Symbol.intern("foo"), result);

        s = new AString("abc");
        result = s.coerce(Symbol.intern("cons"), Nil.NIL);
        assertEquals("cons", result.type().toString());
        ArcObject testlist = (new Cons(Rune.get(0x61), new Cons(Rune.get(0x62), new Cons(Rune.get(0x63), Nil.NIL))));
        assertTrue(testlist.iso(result));

        s = new AString("abc");
        result = s.coerce(Symbol.intern("vector"), Nil.NIL);
        assertEquals("vector", result.type().toString());
        assertTrue((new Vector(Rune.get(0x61), Rune.get(0x62), Rune.get(0x63))).iso(result));
    }
}
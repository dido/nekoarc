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
import com.stormwyrm.nekoarc.ciel.Ciel;
import org.junit.Test;

import static org.junit.Assert.*;

public class SymbolTest {

    @Test
    public void testCoerce() {
        ArcObject sym = Symbol.intern("foo");
        ArcObject result;

        result = sym.coerce(Symbol.intern("sym"), Nil.NIL);
        assertEquals("sym", result.type().toString());
        assertEquals(sym, result);

        result = sym.coerce(Symbol.intern("string"), Nil.NIL);
        assertEquals("string", result.type().toString());
        assertTrue(new AString(sym.toString()).is(result));
    }

    @Test
    public void testMarshal() {
        OutString os = new OutString();
        ArcObject sym = Symbol.intern("foo");
        sym.marshal(os);
        byte[] b = os.insideBytes();
        InString is = new InString(b, "");
        Ciel c = new Ciel(is);
        c.load();
        assertTrue(Symbol.intern("foo").is(c.pop()));
    }

}
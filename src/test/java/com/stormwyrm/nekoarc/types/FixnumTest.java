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

import java.util.Random;

import static org.junit.Assert.*;

public class FixnumTest {
    @Test
    public void testCoerce() {
        Fixnum f = Fixnum.get(123);
        ArcObject result;

        result = f.coerce(Symbol.intern("fixnum"), Nil.NIL);
        assertEquals("fixnum", result.type().toString());
        assertEquals(result, f);

        result = f.coerce(Symbol.intern("flonum"), Nil.NIL);
        assertEquals("flonum", result.type().toString());
        assertEquals(((Flonum)result).flonum, 123.0, 1e-6);

        result = f.coerce(Symbol.intern("string"), Nil.NIL);
        assertEquals("string", result.type().toString());
        assertEquals("123", result.toString());

        result = f.coerce(Symbol.intern("string"), new Cons(Fixnum.get(16), Nil.NIL));
        assertEquals("string", result.type().toString());
        assertEquals("7b", result.toString());

        result = f.coerce(Symbol.intern("string"), new Cons(Fixnum.get(35), Nil.NIL));
        assertEquals("string", result.type().toString());
        assertEquals("3i", result.toString());

        result = f.coerce(Symbol.intern("rune"), Nil.NIL);
        assertEquals("rune", result.type().toString());
        assertEquals("#\\{", result.toString());
    }

    @Test
    public void testMarshal() {
        OutString os = new OutString();
        // Generate 100 random fixnums, and marshal them into os.
        Random rng = new Random();
        ArcObject[] nums = new ArcObject[100];
        for (int i=0; i<100; i++) {
            nums[i] = Fixnum.get(rng.nextLong());
            nums[i].marshal(os);
        }
        InString is = new InString(os.insideBytes(), "");
        // Now, load the marshalled data.
        Ciel c = new Ciel(is);
        c.load();
        // The stack of the Ciel object should contain each of the fixnums we have in nums in reverse order
        for (int i=99; i>=0; i--)
            assertTrue(nums[i].iso(c.pop()));
    }
}
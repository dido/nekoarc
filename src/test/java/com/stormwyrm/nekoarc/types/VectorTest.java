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
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class VectorTest {
    @Test
    public void testToString() {
        int len = 5;
        Vector v = new Vector(len);

        for (int i=0; i<len; i++)
            v.setIndex(i, Fixnum.get(i));
        assertEquals("[0 1 2 3 4]", v.toString());
    }

    @Test
    public void testToStringCyclic() {
        int len = 5;
        Vector v = new Vector(len);

        for (int i=0; i<len; i++)
            v.setIndex(i, Fixnum.get(i));
        v.setIndex(0, v);
        assertEquals("#0=[#0# 1 2 3 4]", v.toString());

        for (int i=0; i<len; i++)
            v.setIndex(i, Fixnum.get(i));
        v.setIndex(1, v);
        assertEquals("#0=[0 #0# 2 3 4]", v.toString());

        Cons x, y;
        Cons c;
        c = new Cons(Fixnum.get(1), y = new Cons(Fixnum.get(2), new Cons(Fixnum.get(3),
                x = new Cons(Fixnum.get(4), Nil.NIL))));
        y.scar(x);
        x.scar(v);
        x.scdr(c);

        for (int i=0; i<len; i++)
            v.setIndex(i, Fixnum.get(i));
        v.setIndex(1, c);
        v.setIndex(4, c);
        assertEquals("#0=[0 #1=(1 #2=(#0# . #1#) 3 . #2#) 2 3 #1#]", v.toString());
    }

    @Test
    public void testIterator() {
        int len = 5;
        Vector v = new Vector(len);

        for (int i=0; i<len; i++)
            v.setIndex(i, Fixnum.get(i));
        Iterator<ArcObject> iter = v.iterator();
        for (int i=0; i<len; i++)
            assertEquals(i, Fixnum.cast(iter.next(), Nil.NIL).fixnum);
        assertFalse(iter.hasNext());
    }

    @Test
    public void testCoerce() {
        Vector v = new Vector(Fixnum.get(1), Fixnum.get(2), Fixnum.get(3));
        ArcObject result;

        result = v.coerce(Symbol.intern("vector"), Nil.NIL);
        assertEquals(v, result);

        result = v.coerce(Symbol.intern("cons"), Nil.NIL);
        assertEquals("cons", result.type().toString());
        assertTrue((new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), Nil.NIL)))).iso(result));

        result = v.coerce(Symbol.intern("string"), Nil.NIL);
        assertEquals("string", result.type().toString());
        assertEquals("123", result.toString());
    }
}
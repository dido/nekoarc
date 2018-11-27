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
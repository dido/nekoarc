package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class ConsTest {
    @Test
    public void testIteration() {
        Cons c = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), Nil.NIL)));
        Iterator<ArcObject> iter = c.iterator();
        int count = 0;
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testToString() {
        Cons c = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), Nil.NIL)));
        assertEquals("(1 2 3)", c.toString());
    }

    @Test
    public void testToStringImproper() {
        Cons c = new Cons(Fixnum.get(1), Fixnum.get(2));
        assertEquals("(1 . 2)", c.toString());

        c = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), new Cons(Fixnum.get(3), Fixnum.get(4))));
        assertEquals("(1 2 3 . 4)", c.toString());
    }

    @Test
    public void testNested() {
        Cons c = new Cons(new Cons(Fixnum.get(1), Fixnum.get(2)), new Cons(Fixnum.get(3), new Cons(Fixnum.get(4), Nil.NIL)));
        assertEquals("((1 . 2) 3 4)", c.toString());
        c = new Cons(new Cons(Fixnum.get(1), Fixnum.get(2)), new Cons(Fixnum.get(3), Fixnum.get(4)));
        assertEquals("((1 . 2) 3 . 4)", c.toString());
    }

    @Test
    public void testIso() {
        Cons c1, c2;

        c1 = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), Nil.NIL));
        c2 = new Cons(Fixnum.get(1), new Cons(Fixnum.get(2), Nil.NIL));
        assertTrue(c1.is(c1));
        assertTrue(c1.iso(c1));
        assertFalse(c1.is(c2));
        assertTrue(c1.iso(c2));
        assertTrue(c2.iso(c1));
        c2 = new Cons(Fixnum.get(1), new Cons(Fixnum.get(3), Nil.NIL));
        assertFalse(c1.is(c2));
        assertFalse(c1.iso(c2));
        assertFalse(c2.iso(c1));

        c2 = new Cons(Fixnum.get(1), Nil.NIL);
        assertFalse(c1.is(c2));
        assertFalse(c1.iso(c2));
        assertFalse(c2.iso(c1));

    }
}
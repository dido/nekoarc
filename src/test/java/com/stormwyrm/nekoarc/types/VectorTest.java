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
        assertEquals("[1 2 3 4 5]", v.toString());
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
}
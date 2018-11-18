package com.stormwyrm.nekoarc.types;

import org.junit.Test;

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

}
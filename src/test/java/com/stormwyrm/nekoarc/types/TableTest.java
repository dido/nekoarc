package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
import org.junit.Test;

import static org.junit.Assert.*;

public class TableTest {
    @Test
    public void testCoerce() {
        ArcObject t = new Table();
        ArcObject result;

        result = t.coerce(Symbol.intern("table"), Nil.NIL);
        assertEquals("table", result.type().toString());
        assertEquals(t, result);

        result = t.coerce(Symbol.intern("string"), Nil.NIL);
        assertEquals("string", result.type().toString());
        assertEquals("#hash()", result.toString());
    }

}
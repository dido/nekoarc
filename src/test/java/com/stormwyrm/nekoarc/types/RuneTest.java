package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
import org.junit.Test;

import static org.junit.Assert.*;

public class RuneTest {
    @Test
    public void testCoerce() {
        Rune r = Rune.get(0x9f8d);
        ArcObject result;

        result = r.coerce(Symbol.intern("rune"), Nil.NIL);
        assertEquals("rune", result.type().toString());
        assertEquals(result, r);

        result = r.coerce(Symbol.intern("fixnum"), Nil.NIL);
        assertEquals("fixnum", result.type().toString());
        assertEquals(result, Fixnum.get(0x9f8d));

        result = r.coerce(Symbol.intern("string"), Nil.NIL);
        assertEquals("string", result.type().toString());
        assertTrue(result.is(new AString("龍")));
    }
}
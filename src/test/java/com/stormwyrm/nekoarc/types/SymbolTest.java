package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
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

}
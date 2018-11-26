package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlonumTest {
    @Test
    public void testCoerce() {
        Flonum f = new Flonum(3.14159265);
        ArcObject result;

        result = f.coerce(Symbol.intern("flonum"), Nil.NIL);
        assertEquals("flonum", result.type().toString());
        assertEquals(((Flonum)result).flonum, 3.14159265, 1e-6);

        result = f.coerce(Symbol.intern("fixnum"), Nil.NIL);
        assertEquals("fixnum", result.type().toString());
        assertEquals(3, ((Fixnum)result).fixnum);

        result = f.coerce(Symbol.intern("string"), Nil.NIL);
        assertEquals("string", result.type().toString());
        assertEquals("3.14159265", result.toString());
    }

}
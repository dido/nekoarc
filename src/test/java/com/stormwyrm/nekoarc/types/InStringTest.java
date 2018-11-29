package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
import org.junit.Test;

import static org.junit.Assert.*;

public class InStringTest {
    @Test
    public void testReadC() {
        // 0x61 0x62 0x63 = one byte
        // 0xde 0x03c9 0x0e26 = two bytes
        // 0x86df 0x9f8d 0xa9c5 = three bytes
        // 0x100f6 0x1d11e 0x1f605 = four bytes
        InString ins = new InString("abcÞωฦ蛟龍꧅\uD800\uDCF6\uD834\uDD1E\uD83D\uDE05");

        ArcObject obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x61, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x62, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x63, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xde, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x3c9, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xe26, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x86df, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x9f8d, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xa9c5, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x100f6, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x1d11e, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x1f605, ((Rune)obj).rune);

        obj = ins.readc();
        assertTrue(Nil.NIL.is(obj));
    }

}
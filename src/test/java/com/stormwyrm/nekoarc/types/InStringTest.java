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

    @Test
    public void testInvalid() {
        byte[] invalid = {
                (byte) 0xc3, (byte) 0xb1,                // valid 2-octet
                (byte) 0xc3, (byte) 0x28,               // invalid 2-octet
                (byte) 0xa0,                            // invalid sequence identifier
                (byte) 0xa1,                            // invalid sequence identifier
                (byte) 0xe2, (byte) 0x82, (byte) 0xa1,  // valid 3-octet
                (byte) 0xe2, 0x28, (byte) 0xa1,          // invalid 3-octet in 2nd octet
                (byte) 0xe2, (byte)0x82, (byte)0x28,     // invalid 3-octet in 3rd octet
                (byte) 0xf0, (byte)0x90, (byte)0x8c, (byte)0xbc,    // valid 4-octet
                (byte) 0xf0, (byte)0x28, (byte)0x8c, (byte)0xbc,    // invalid 4-octet in 2nd octet
                (byte) 0xf0, (byte)0x90, (byte)0x28, (byte)0xbc,    // invalid 4-octet in 3rd octet
                (byte) 0xf0, (byte)0x90, (byte)0x8c, (byte)0x28,    // invalid 4-octet in 4th octet
        };

        InString ins = new InString(invalid, "");

        ArcObject obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xf1, ((Rune)obj).rune);

        // Invalid 2-octet
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xfffd, ((Rune)obj).rune);

        // invalid sequence identifier
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xfffd, ((Rune)obj).rune);

        // invalid sequence identifier
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xfffd, ((Rune)obj).rune);

        // valid 3-octet
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x20a1, ((Rune)obj).rune);

        // invalid 3-octet in 2nd octet
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xfffd, ((Rune)obj).rune);

        // invalid 3-octet in 3nd octet
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xfffd, ((Rune)obj).rune);

        // valid 4-octet
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0x1033c, ((Rune)obj).rune);

        // invalid 4-octet in 2nd octet
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xfffd, ((Rune)obj).rune);

        // invalid 4-octet in 3rd octet
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xfffd, ((Rune)obj).rune);

        // invalid 4-octet in 4th octet
        obj = ins.readc();
        assertTrue(obj instanceof Rune);
        assertEquals(0xfffd, ((Rune)obj).rune);

        // end of string
        obj = ins.readc();
        assertTrue(Nil.NIL.is(obj));
    }
}
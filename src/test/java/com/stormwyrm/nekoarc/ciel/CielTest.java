/*
 * Copyright (c) 2019 Rafael R. Sevilla
 *
 * This file is part of NekoArc
 *
 * NekoArc is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.stormwyrm.nekoarc.ciel;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.InString;
import com.stormwyrm.nekoarc.types.OutString;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class CielTest {

    private static void genericTest(long value, int nbytes) {
        OutString os = new OutString();
        CAsm.writeLong(os, value);
        byte[] bytes = os.insideBytes();
        assertEquals(nbytes, bytes.length);

        InString is = new InString(bytes, "");
        Ciel c = new Ciel(is);
        assertEquals(value, c.readLong());
    }

    private static void test100Nums(Random rng, int nbits) {
        long ul = (1L << (nbits - 8)) - 1;
        long ll = -(1L << (nbits - 8));
        long bytespernum = nbits / 7;
        long[] numbers = new long[100];
        long mask = (1L << (nbits)) - 1;
        long reduce = (1L << (nbits-1));

        OutString os = new OutString();
        for (int i=0; i<100; i++) {
            long num;
            do {
                num = rng.nextLong();
                // Now, we need to mask off the excess bits so that nbits at most are
                // used.
                num &= mask;
                // This gets us an nbits positive integer. Consider also negative numbers
                // in the same range
                num -= reduce;
                // Reject numbers that have can be represented by nbits - 7 bits or fewer
            } while (num >= ll && num <= ul);
            numbers[i] = num;
            CAsm.writeLong(os, num);
        }
        byte[] bytes = os.insideBytes();
        assertEquals(100 * bytespernum, bytes.length);
        InString is = new InString(bytes, "");
        Ciel c = new Ciel(is);
        for (int i=0; i<100; i++)
            assertEquals(numbers[i], c.readLong());
    }

    @Test
    public void testRWLong() {
        OutString os;

        // Test 0
        os = new OutString();
        CAsm.writeLong(os, 0);
        byte[] bytes = os.insideBytes();
        assertEquals(1, bytes.length);
        assertEquals((byte)0x80, bytes[0]);

        InString is = new InString(bytes, "");
        Ciel c = new Ciel(is);
        assertEquals(0, c.readLong());

        // Test -1
        os = new OutString();
        CAsm.writeLong(os, -1);
        bytes = os.insideBytes();
        assertEquals(1, bytes.length);
        assertEquals((byte)0xff, bytes[0]);

        is = new InString(bytes, "");
        c = new Ciel(is);
        assertEquals(-1, c.readLong());

        // Generate all two's complement 7-bit numbers (-64 to +63) and make sure that they pack
        // and unpack properly.
        os = new OutString();
        for (int i=-64; i<=63; i++)
            CAsm.writeLong(os, i);
        bytes = os.insideBytes();
        // All 7-bit numbers should marshal into a single byte, so the bytes should be equal to exactly 128
        assertEquals(128, bytes.length);

        // Read them back in
        is = new InString(bytes, "");
        c = new Ciel(is);
        for (int i=-64; i<=63; i++)
            assertEquals(i, c.readLong());

        // Test the boundaries. +64 should need two octets
        genericTest(64, 2);

        // -65 should also need two octets
        genericTest(-65, 2);

        // Generate a hundred 14-bit numbers, all of which should marshal to two bytes
        Random rng = new Random(0x8f05decd5f11dd6bL);
        test100Nums(rng, 14);

        // Boundary test. 8191 should be two bytes, as should -8192
        genericTest(8191, 2);
        genericTest(-8192, 2);

        // But +8191 and -8193 should be three
        genericTest(8192, 3);
        genericTest(-8193, 3);

        // Next, generate and test another hundred random 21-bit numbers
        test100Nums(rng, 21);

        // Boundary tests. 1048575 should be three bytes, as is -1048576
        genericTest(1048575, 3);
        genericTest(-1048576, 3);

        // 1048576 should be four bytes, as -1048577
        genericTest(1048576, 4);
        genericTest(-1048577, 4);

        test100Nums(rng, 28);

        // More boundary tests. 2^27 -1 (134,217,727) and -2^27 (-134,217,728) should be four bytes
        genericTest(134217727, 4);
        genericTest(-134217728, 4);

        // 2^27 and -2^27 - 1 should go to five
        genericTest(134217728, 5);
        genericTest(-134217729, 5);

        test100Nums(rng, 35);

        // 2^34-1 (17,179.869,183) and -2^34 (17,179.869,184) should still be 5
        genericTest(17179869183L, 5);
        genericTest(-17179869184L, 5);

        // but 2^34 and -2^34 - 1 should go to 6
        genericTest(17179869184L, 6);
        genericTest(-17179869185L, 6);

        test100Nums(rng, 42);

        // 2^41-1 and -2^41 should still be 6
        genericTest(2199023255551L, 6);
        genericTest(-2199023255552L, 6);

        // but 2^41 and -2^41 - 1 should go to 6
        genericTest(2199023255552L, 7);
        genericTest(-2199023255553L, 7);

        test100Nums(rng, 49);

        // 2^48-1 and -2^48 should still be 7
        genericTest(281474976710655L, 7);
        genericTest(-281474976710656L, 7);

        // but 2^48 and -2^48 - 1 should go to 8
        genericTest(281474976710656L, 8);
        genericTest(-281474976710657L, 8);

        test100Nums(rng, 56);

        // 2^55-1 and -2^55 should still be 7
        genericTest(36028797018963967L, 8);
        genericTest(-36028797018963968L, 8);

        // but 2^55 and -2^55 - 1 should go to 8
        genericTest(36028797018963968L, 9);
        genericTest(-36028797018963969L, 9);

        test100Nums(rng, 63);

        // 2^62-1 and -2^62 should still be 9
        genericTest(4611686018427387903L, 9);
        genericTest(-4611686018427387904L, 9);

        // but 2^62 and -2^62 - 1 should go to 10
        genericTest(4611686018427387904L, 10);
        genericTest(-4611686018427387905L, 10);

        // Generate 100 numbers between -2^62-1 and -2^63 or 2^62 and 2^63-1
        os = new OutString();
        long[] numbers = new long[100];
        for (int i=0; i<100; i++) {
            long num = rng.nextLong();

            // make sure that this number is between the above ranges by setting the high
            // bits appropriately.
            if (num >= 0)
                num |= 0x4000000000000000L;
            else
                num &= 0xbfffffffffffffffL;

            assertTrue(num >= 4611686018427387904L || num <= -4611686018427387905L);

            numbers[i] = num;
            CAsm.writeLong(os, num);
        }
        bytes = os.insideBytes();
        assertEquals(100 * 10, bytes.length);
        is = new InString(bytes, "");
        c = new Ciel(is);
        for (int i=0; i<100; i++)
            assertEquals(numbers[i], c.readLong());

        // 2^63-1 and -2^63 should go to 10, these are the maximum values supported
        genericTest(9223372036854775807L, 10);
        genericTest(-9223372036854775808L, 10);

        // try decoding a random 11-byte value, this should produce an error
        bytes = new byte[11];
        for (int i=0; i<10; i++)
            bytes[i] = (byte) (rng.nextInt() & 0x7f);
        bytes[10] = (byte) (rng.nextInt() | 0x80);
        is = new InString(bytes, "");
        c = new Ciel(is);
        try {
            c.readLong();
            fail("exception not thrown");
        } catch (NekoArcException e) {
            assertEquals("long value exceeded", e.getMessage());
        }
    }

    @Test
    public void testRWDouble() {
        Random rng = new Random();
        double value = rng.nextDouble();
        OutString os = new OutString();
        CAsm.writeDouble(os, value);
        byte[] bytes = os.insideBytes();
        assertEquals(8, bytes.length);

        InString is = new InString(bytes, "");
        Ciel c = new Ciel(is);
        // The conversion should ALWAYS be exact, so these must always be equal
        assertEquals(Double.doubleToLongBits(value), Double.doubleToLongBits(c.readDouble()));
    }

}
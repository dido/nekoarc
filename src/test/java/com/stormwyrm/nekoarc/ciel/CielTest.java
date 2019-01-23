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

import com.stormwyrm.nekoarc.types.InString;
import com.stormwyrm.nekoarc.types.OutString;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class CielTest {
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

        // Marshal a number that requires 8 bits, which should be two bytes when marshalled
        // Test -1
        os = new OutString();
        CAsm.writeLong(os, 65);
        bytes = os.insideBytes();
        assertEquals(2, bytes.length);

        is = new InString(bytes, "");
        c = new Ciel(is);
        assertEquals(65, c.readLong());

        // Generate a hundred 14-bit numbers, all of which should marshal to two bytes
        Random rng = new Random(0x8f05decd5f11dd6bL);
        long[] numbers = new long[100];
        os = new OutString();
        for (int i=0; i<100; i++) {
            long num;
            do {
                num = rng.nextLong();
                num &= 0x3fff;
                num -= 0x2000;
                // Reject numbers that have can be represented with at most seven bits
            } while (num >= -128 && num < 128);
            numbers[i] = num;
            CAsm.writeLong(os, num);
            System.out.println(num);
        }
        bytes = os.insideBytes();
        // All 14-bit numbers should marshal to two bytes, so the byte length should be exactly 200
        assertEquals(200, bytes.length);
        is = new InString(bytes, "");
        c = new Ciel(is);
        for (int i=0; i<100; i++)
            assertEquals(numbers[i], c.readLong());
    }

}
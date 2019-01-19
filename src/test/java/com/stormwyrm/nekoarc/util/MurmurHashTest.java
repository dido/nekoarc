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

package com.stormwyrm.nekoarc.util;

import static org.junit.Assert.*;
import com.stormwyrm.nekoarc.util.MurmurHash;
import org.junit.Test;

public class MurmurHashTest {
    @Test
    public void test() {
        assertEquals(0xe34bbc7bbc071b6cL, MurmurHash.hash("The quick brown fox jumps over the lazy dog", 0));
        assertEquals(0x658ca970ff85269aL, MurmurHash.hash("The quick brown fox jumps over the lazy cog", 0));
        assertEquals(0x738a7f3bd2633121L, MurmurHash.hash("The quick brown fox jumps over the lazy dog", 0x9747b28cL));
        assertEquals(0xb8cd57b070826194L, MurmurHash.hash("The quick brown fox jumps over the lazy cog", 0x9747b28cL));
    }
}
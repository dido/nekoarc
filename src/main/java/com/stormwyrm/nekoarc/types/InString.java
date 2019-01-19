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
package com.stormwyrm.nekoarc.types;

import java.io.ByteArrayInputStream;

public class InString extends InputPort {
    private class BIS extends ByteArrayInputStream {
        public BIS(byte[] buf) {
            super(buf);
        }

        public long seek(long newpos) {
            return(pos = (int)newpos);
        }

        public long tell() {
            return(pos);
        }

    }

    private final BIS str;

    public InString(byte[] b, String name) {
        super(name);
        this.str = new BIS(b) {
        };
    }


    public InString(String s, String name) {
        this(s.getBytes(), name);
    }

    public InString(String s) {
        this(s, "");
    }

    @Override
    public long seek(long newpos) {
        return(str.seek(newpos));
    }

    @Override
    public long tell() {
        return(str.tell());
    }

    @Override
    public int readb() {
        // For closed checks
        super.readb();
        return(str.read());
    }
}

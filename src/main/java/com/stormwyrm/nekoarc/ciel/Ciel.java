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
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.InputPort;

/**
 * CIEL virtual machine context
 */
public class Ciel {
    private final static int DEFAULT_STACKSIZE = 1024;
    private final ArcObject[] stack;        // stack
    private int sp;
    private final InputPort fp;

    public Ciel(InputPort fp, int stacksize) {
        stack = new ArcObject[stacksize];
        sp = 0;
        this.fp = fp;
    }

    public Ciel(InputPort fp) {
        this(fp, DEFAULT_STACKSIZE);
    }

    public InputPort port() { return(fp); }

    public void push(ArcObject obj) {
        try {
            stack[sp++] = obj;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NekoArcException("Stack overflow in CIEL");
        }
    }

    public ArcObject pop() {
        return(stack[--sp]);
    }

    public double readDouble() {
        long raw = 0;
        for (int i=0; i<8; i++) {
            raw <<= 8;
            raw |= (fp.readb() & 0xff);
        }
        return(Double.longBitsToDouble(raw));
    }

    public long readLong() {
        long val = 0, x;
        int shiftcount = 0;
        byte b;

        for (;;) {
            b = (byte) (fp.readb() & 0xff);
            if ((b & 0x80) != 0)
                break;
            if (shiftcount > 63)
                throw new NekoArcException("long value exceeded");
            x = b;
            x <<= shiftcount;
            val |= x;
            shiftcount += 7;
        }
        if ((b & 0xc0) == 0x80) {
            // non-negative, clear the topmost bit only
            b ^= 0x80;
        }
        x = b;
        x <<= shiftcount;
        val |= x;
        return(val);
    }
}

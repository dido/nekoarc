/*  Copyright (C) 2018 Rafael R. Sevilla

    This file is part of NekoArc

    NekoArc is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 3 of the
    License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;

public abstract class InputPort extends IOPort {
    public static final ArcObject TYPE = Symbol.intern("input");
    private ArcObject ungetrune = Nil.NIL;

    /**
     * Create an input port with the given name
     *
     * @param name the name (path) of the file
     */
    InputPort(String name) {
        super(name);
    }

    /**
     * Type
     * @return the type (input)
     */
    @Override
    public ArcObject type() {
        return(TYPE);
    }

    /**
     * Read a byte from the input port
     * @return a byte from the input port as an int or -1 if we are at end of file.
     */
    public int readb() {
        if (closedp())
            throw new NekoArcException("readb: input port is closed");
        return(-1);
    }

    /* UTF-8 decoding constants */
    private static final int
            Bitx    = 6,
            Bit2    = 5,
            Bit3    = 4,
            Bit4    = 3,
            Bit5    = 2,			            /* Added for RFC 2279 compliance */
            Tx	    = ((1<<(Bitx+1))-1) ^ 0xFF, /* 1000 0000 */
            T2	    = ((1<<(Bit2+1))-1) ^ 0xFF, /* 1100 0000 */
            T3	    = ((1<<(Bit3+1))-1) ^ 0xFF, /* 1110 0000 */
            T4	    = ((1<<(Bit4+1))-1) ^ 0xFF, /* 1111 0000 */
            T5      = ((1<<(Bit5+1))-1) ^ 0xFF; /* 1111 1000 */

    /**
     * Read a rune from the input port
     * @return a rune from the input port or nil if we are at end of file
     */
    public ArcObject readc() {
        if (closedp())
            throw new NekoArcException("readc: input port is closed");
        if (!Nil.NIL.is(ungetrune)) {
            ArcObject ug = ungetrune;
            ungetrune = Nil.NIL;
            return(ug);
        }
        int c = readb();
        if (c < 0)
            return(Nil.NIL);
        // One-character sequence: 00000-0007F => T1
        if (c < Tx)
            return(Rune.get(c));

        int nbytes;
        // Two-character sequence: 0080-07FF => T2 Tx
        if (c < T3 && c >= T2) {
            nbytes = 2;
            // Three-character sequence: 0x800-FFFF => T3 Tx Tx
        } else if (c < T4 && c >= T3) {
            nbytes = 3;
            // Four-character sequence: 0x00010000- 0x001FFFFF => T4 Tx Tx Tx
        } else if (c < T5 && c >= T4) {
            nbytes = 4;
            // invalid non-Unicode sequence
        } else {
            return (Rune.get(0xfffd));
        }
        byte[] utfbytes = new byte[nbytes];
        utfbytes[0] = (byte)c;
        nbytes--;
        for (int i=0; i<nbytes; i++) {
            int ch = readb();
            if (ch < 0)
                break;
            utfbytes[i + 1] = (byte) (ch & 0xff);
        }
        return(Rune.get((new String(utfbytes)).codePointAt(0)));
    }

    /**
     * Unget a rune
     * @param r the rune to push back into the port
     * @return the rune r
     */
    public ArcObject ungetc(Rune r) {
        if (closedp())
            throw new NekoArcException("ungetc: input port is closed");
        return(ungetrune = r);
    }

    /**
     * Peek at the next rune available
     * @return the next rune, or nil if we are at end of file
     */
    public ArcObject peekc() {
        if (closedp())
            throw new NekoArcException("peekc: input port is closed");
        ArcObject r = readc();
        if (!Nil.NIL.is(r))
            ungetc((Rune)r);
        return(r);
    }

    /**
     * Return a string version of the input port, including the name.
     * @return string version of the input port
     */
    @Override
    public String toString() {
        return("#<input-port:" + getName() + ">");
    }
}

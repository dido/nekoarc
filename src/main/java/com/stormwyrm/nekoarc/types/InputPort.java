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
        return(Nil.NIL);
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

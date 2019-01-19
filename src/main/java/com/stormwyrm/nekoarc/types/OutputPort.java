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

import com.stormwyrm.nekoarc.NekoArcException;

public abstract class OutputPort extends IOPort {
    public static final ArcObject TYPE = Symbol.intern("output");

    /**
     * Create an output port with the given name
     *
     * @param name the name (path) of the file
     */
    OutputPort(String name) {
        super(name);
    }

    /**
     * Type
     * @return the type (output)
     */
    @Override
    public ArcObject type() {
        return(TYPE);
    }

    /**
     * Write a byte to the output port
     * @param b the byte to write
     * @return the byte written
     */
    public int writeb(int b) {
        if (closedp())
            throw new NekoArcException("writeb: output port is closed");
        return(b);
    }

    /**
     * Write a rune (character) to the output port
     * @param r the rune to write
     * @return the rune written
     */
    public ArcObject writec(Rune r) {
        if (closedp())
            throw new NekoArcException("writec: output port is closed");
        byte[] charbytes = String.format("%c", r.rune).getBytes();
        for (byte b : charbytes)
            writeb(b);
        return(r);
    }

    /**
     * Return a string version of the output port, including the name.
     * @return string version of the output port
     */
    @Override
    public String toString() {
        return("#<output-port:" + getName() + ">");
    }
}

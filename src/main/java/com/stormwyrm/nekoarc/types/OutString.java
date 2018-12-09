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

import java.io.ByteArrayOutputStream;

public class OutString extends OutputPort {
    private ByteArrayOutputStream outstr;

    /**
     * Create an output string with the given name
     *
     * @param name the name (path) of the file
     */
    public OutString(String name) {
        super(name);
        outstr = new ByteArrayOutputStream();
    }


    /**
     * Create an output string with no name
     */
    public OutString() {
        this("");
    }

    /**
     * Write a byte to the string
     * @param b the byte to write
     * @return the byte written
     */
    @Override
    public int writeb(int b) {
        super.writeb(b);
        outstr.write(b);
        return(b);
    }

    /**
     * Get the string inside the outstring.
     * @return the string inside it
     */
    public ArcObject inside() {
        return(new AString(outstr.toString()));
    }
}

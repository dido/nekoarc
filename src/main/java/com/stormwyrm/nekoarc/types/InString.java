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

import com.stormwyrm.nekoarc.Nil;

public class InString extends InputPort {
    private String str;
    private int ptr;

    public InString(String s) {
        this(s, "");
    }

    public InString(String s, String name) {
        super(name);
        this.str = s;
        this.ptr = 0;
    }

    @Override
    public int readb() {
        // Note this will still return the Unicode code points. Reference Arc returns UTF-8 bytes!
        if (ptr >= str.length())
            return(-1);
        return(str.codePointAt(ptr++));
    }

    @Override
    public ArcObject readc() {
        // call super for ungetrune handling
        ArcObject r = super.readc();
        if (!Nil.NIL.is(r))
            return(r);
        if (ptr >= str.length())
            return(Nil.NIL);
        return(Rune.get(str.codePointAt(ptr++)));
    }
}

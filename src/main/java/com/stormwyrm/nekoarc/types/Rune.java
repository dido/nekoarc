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
import com.stormwyrm.nekoarc.util.LongMap;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class Rune extends ArcObject {
    public final ArcObject TYPE = Symbol.intern("rune");
    public final int rune;
    private static final LongMap<WeakReference<Rune>> table = new LongMap<>();
    private static final ReferenceQueue<Rune> rq = new ReferenceQueue<>();

    private Rune(int x) { rune = x; }

    public static Rune get(int x) {
        Rune r;

        if (x < 0)
            throw new NekoArcException("invalid rune index");
        WeakReference<Rune> wrf = table.get(x);
        if (wrf != null) {
            r = wrf.get();
            if (r != null)
                return (r);
        }
        r = new Rune(x);
		table.put(x, new WeakReference<>(r, rq));
		return(r);
    }

    @Override
    public ArcObject type()
    {
        return(TYPE);
    }

    @Override
    public String toString() {
        switch (rune) {
            case 0x08:
                return("#\\backspace");
            case 0x09:
                return("#\\tab");
            case 0x0a:
                return("#\\newline");
            case 0x0b:
                return("#\\vtab");
            case 0x0c:
                return("#\\page");
            case 0x0d:
                return("#\\return");
            case 0x20:
                return("#\\space");
            case 0x7f:
                return("#\\rubout");
            default:
                if (rune < 0x0020 || (rune > 0x007f && rune < 0x0100))
                    return(String.format("#\\#u%04x", rune));
                break;
        }
        return(String.format("#\\%c", rune));
    }
}

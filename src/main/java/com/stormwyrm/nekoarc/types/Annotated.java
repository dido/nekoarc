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

public class Annotated extends ArcObject {
    private ArcObject atype;
    private ArcObject arep;

    public Annotated(ArcObject t, ArcObject r) {
        atype = t;
        arep = r;
    }

    @Override
    public ArcObject type() {
        return(atype);
    }

    @Override
    public ArcObject rep() {
        return(arep);
    }

    @Override
    public String toString() {
        return("#(tagged " + atype + " " + arep + ")");
    }
}

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
package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;

/**
 * Unbound object
 */
public class Unbound extends ArcObject {
	public static final ArcObject TYPE = Symbol.intern("unbound");
	public static final Unbound UNBOUND = new Unbound();

	private Unbound() { }

	@Override
	public ArcObject type() {
		return(TYPE);
	}

	@Override
	public boolean is(ArcObject other) {
		return(this == other || (other instanceof Unbound));
	}

	@Override
	public String toString() {
		return("#<unbound>");
	}
}

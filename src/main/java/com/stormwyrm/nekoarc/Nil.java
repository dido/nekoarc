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

import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.util.ObjectMap;

/**
 * Nil class
 */
public class Nil extends Cons {
	public static final ArcObject TYPE = Symbol.TYPE;
	public static final Nil NIL = new Nil("nil");
	public static final Nil EMPTY_LIST = new Nil("()");
	private String rep;

	private Nil(String rep) {
		this.rep = rep;
	}

	public String toString() {
		return(rep);
	}

	@Override
	public long len() {
        return (0);
    }

    @Override
	public boolean is(ArcObject other) {
		return(this == other || (other instanceof Nil));
	}

	@Override
	public boolean iso(ArcObject object, ObjectMap<ArcObject, ArcObject> seen) {
		return(is(object));
	}

	@Override
	public void visit(ObjectMap<ArcObject, ArcObject> seen, int[] counter) { }

	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("string"))
			return(new AString(""));

		return(super.coerce(newtype, extra));
	}
}

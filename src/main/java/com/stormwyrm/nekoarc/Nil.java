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
package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.ciel.CAsm;
import com.stormwyrm.nekoarc.types.*;
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

	/**
	 * Get string representation of a nil
	 * @return The string representation
	 */
	@Override
	public String toString() {
		return(rep);
	}

	/**
	 * Get string representation of a nil with a seen hash.  This will return its representation.
	 * @param seen the seen hash
	 * @return The string representation.
	 */
	@Override
	public String toString(ObjectMap<ArcObject, ArcObject> seen) {
		return(toString());
	}

	/**
	 * Length of a nil
	 * @return Always 0
	 */
	@Override
	public long len() {
        return (0);
    }

	/**
	 * Do a comparison of nil to something else
	 * @param other The object to compare
	 * @return True if the other object is also nil.
	 */
	@Override
	public boolean is(ArcObject other) {
		return(this == other || (other instanceof Nil));
	}

	/**
	 * Do a structural comparison of nil to something
	 * @param object The object to compare
	 * @param seen Seen hash
	 * @return True if the other object is also nil
	 */
	@Override
	public boolean iso(ArcObject object, ObjectMap<ArcObject, ArcObject> seen) {
		return(is(object));
	}

	/**
	 * Do a structural comparison of nil to something
	 * @param other the object to compare with
	 * @return True if the other object is also nil
	 */
	@Override
	public boolean iso(ArcObject other) {
		return(is(other));
	}

	/**
	 * Visit a nil. This does absolutely nothing.
	 * @param seen The seen hash
	 * @param counter The counter
	 */
	@Override
	public void visit(ObjectMap<ArcObject, ArcObject> seen, int[] counter) { }

	/**
	 * Coerce a nil to some other type.  Only valid conversion is to string.
	 * @param newtype The new type to convert to
	 * @param extra additional parameters for the conversion, if any
	 * @return The nil converted to the new type
	 */
	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("string"))
			return(new AString(""));

		return(super.coerce(newtype, extra));
	}

	@Override
	public void marshal(OutputPort p, ObjectMap<ArcObject, ArcObject> seen) {
		marshal(p);
	}

	@Override
	public void marshal(OutputPort p) {
		CAsm.GNIL.emit(p);
	}
}

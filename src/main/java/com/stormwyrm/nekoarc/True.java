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
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.InputPort;
import com.stormwyrm.nekoarc.types.OutputPort;
import com.stormwyrm.nekoarc.types.Symbol;

/**
 * True class
 */
public class True extends ArcObject {
	public static final ArcObject TYPE = Symbol.TYPE;
	public static final True T = new True("t");
	private final String rep;

	/**
	 * Create a true. This is a singleton so private
	 * @param rep Representation as a sym
	 */
	private True(String rep) {
		this.rep = rep;
	}

	/**
	 * Get the type
	 * @return Symbol.TYPE
	 */
	@Override
	public ArcObject type() {
		return(Symbol.TYPE);
	}

	/**
	 * Convert to a string
	 * @return 't'
	 */
	public String toString() {
		return(rep);
	}

	/**
	 * Is other the same as this True
	 * @param other The object to compare
	 * @return boolean True if True
	 */
	@Override
	public boolean is(ArcObject other) {
		return(this == other || (other instanceof True) || (other instanceof Symbol && ((Symbol)other).symbol.equals(this.rep)));
	}

	/**
	 * Marshal the true object
	 * @param p The port to write to
	 */
	@Override
	public void marshal(OutputPort p) {
		CAsm.GTRUE.emit(p);
	}

	@Override
	public ArcObject load(InputPort p) {
		return(this);
	}
}

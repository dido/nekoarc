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
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.Vector;
import com.stormwyrm.nekoarc.types.ArcThread;

/** Heap environment */
public class HeapEnv extends Vector {
	public static final ArcObject TYPE = Symbol.intern("environment");
	private ArcObject prev;

	/**
	 * Create a new heap environment
	 * @param size The size of the environmment
	 * @param p The previous environment
	 */
	public HeapEnv(int size, ArcObject p) {
		super(size);
		prev = p;
	}

	/**
	 * Get the previous environment
	 * @return The previous environment
	 */
	public ArcObject prevEnv()
	{
		return(prev);
	}

	/**
	 * Get an environment value
	 * @param index Index into the environment
	 * @return The value at that index
	 */
	public ArcObject getEnv(int index) {
		return(this.index(index));
	}

	/**
	 * Set a value in the environment
	 * @param index Index into the environment
	 * @param value Tbe new value of to set at that index
	 * @return The new value that had been set
	 */
	public ArcObject setEnv(int index, ArcObject value) {
		return(setIndex(index, value));
	}

	/**
	 * Convert a stack-based environment into a heap-based one. Also affects any environments
	 * linked to it.
	 * @param thr Thread in which the environment exists
	 * @param sei The environment to convert
	 * @return The new heap-based environment
	 */
	public static ArcObject fromStackEnv(ArcThread thr, ArcObject sei) {
		if (sei instanceof HeapEnv || sei.is(Nil.NIL))
			return(sei);
		int si = (int)((Fixnum)sei).fixnum;
		int start = (int)((Fixnum)thr.stackIndex(si)).fixnum;
		int size = (int)((Fixnum)thr.stackIndex(si+1)).fixnum;
		ArcObject penv = thr.stackIndex(si+2);
		// Convert previous env into a stack-based one too
		penv = fromStackEnv(thr, penv);
		HeapEnv nenv = new HeapEnv(size, penv);
		// Copy stack env data to new env
		for (int i=0; i<size; i++)
			nenv.setEnv(i, thr.stackIndex(start + i));
		return(nenv);
	}
}

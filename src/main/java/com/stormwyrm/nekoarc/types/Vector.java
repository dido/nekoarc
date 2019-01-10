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

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.util.ObjectMap;

import java.util.Iterator;

/**
 * Vectors
 */
public class Vector extends ArcObject implements Iterable<ArcObject> {
	public static final ArcObject TYPE = Symbol.intern("vector");
	private ArcObject[] vec;

	public Vector(int length) {
		vec = new ArcObject[length];
	}

	public Vector(ArcObject... data) {
		this(data.length);
		System.arraycopy(data, 0, vec, 0, data.length);
	}

	public ArcObject index(int i) {
		return(vec[i]);
	}

	public ArcObject setIndex(int i, ArcObject val) {
		return(vec[i] = val);
	}

	@Override
	public long len() {
		return(vec.length);
	}

	@Override
	public ArcObject type() {
		return(TYPE);
	}

	/**
	 * Visit this vector. Will also call visit for the objects inside the vector.
	 * @param seen The seen hash
	 * @param counter Counter for seen hash
	 */
	@Override
	public void visit(ObjectMap<ArcObject, ArcObject> seen, int[] counter) {
		if (seen.containsKey(this)) {
			ArcObject val = seen.get(this);
			if (Nil.NIL.is(val)) {
				seen.put(this, new Cons(Fixnum.get(counter[0]), Nil.NIL));
				counter[0] += 1;
			}
			return;
		}
		seen.put(this, Nil.NIL);
		for (ArcObject obj : vec)
			obj.visit(seen, counter);
	}

	/**
	 * Get the string representation with a seen hash. Uses sharpsign-equalsign notation for
	 * recursive structures.
	 * @param seen the seen hash
	 * @return The string representation of the vector
	 */
	@Override
	public String toString(ObjectMap<ArcObject, ArcObject> seen) {
		ArcObject s=Nil.NIL;
		if (seen.containsKey(this) && !Nil.NIL.is(s = seen.get(this)) && !Nil.NIL.is(s.cdr()))
			return("#" + s.car().toString() + "#");
		StringBuilder sb = new StringBuilder();
		if (!Nil.NIL.is(s)) {
			sb.append("#");
			sb.append(s.car().toString());
			sb.append("=");
			s.scdr(True.T);
		}
		sb.append("[");
		int i=0;
		while (i<vec.length) {
			sb.append(vec[i].toString(seen));
			if (i<vec.length-1)
				sb.append(" ");
			i++;
		}
		sb.append("]");
		return(sb.toString());
	}

	/**
	 * Get the string representation of the vector. Will visit the vector and its children to
	 * build a seen hash and use the seen hash version to print
	 * @return The string representation of the vector.
	 */
	@Override
	public String toString() {
		ObjectMap<ArcObject, ArcObject> seen = new ObjectMap<>();
		this.visit(seen, new int[]{0});
		return(toString(seen));
	}

	/**
	 * Structural equality predicate
	 * @param other the object to compare with
	 * @param seen the seen hash
	 * @return true this is structurally equal to other
	 */
	@Override
	public boolean iso(ArcObject other, ObjectMap<ArcObject, ArcObject> seen) {
		// if the seen hash is set for one but not the other, they obviously can't be the same
		if (seen.containsKey(this) ^ seen.containsKey(other))
			return(false);
		// If the seen hash is set for both, then they are not different thus far
		if (seen.containsKey(this) && seen.containsKey(other))
			return(true);
		seen.put(this, True.T);
		seen.put(other, True.T);
		if (!(other instanceof Vector))
			return(false);
		Vector v2 = (Vector)other;
		if (v2.len() != this.len())
			return(false);
		for (int i=0; i<this.len(); i++) {
			if (!v2.index(i).iso(this.index(i), seen))
				return(false);
		}
		return(true);
	}

	@Override
	public boolean iso(ArcObject other) {
		return(iso(other, new ObjectMap<>()));
	}

    @Override
	public int requiredArgs() {
		return(1);
	}

	@Override
    public ArcObject sref(ArcObject value, ArcObject index) {
	    return(this.setIndex((int) Fixnum.cast(index, this).fixnum, value));
    }

    @Override
	public ArcObject invoke(InvokeThread ithr)
	{
		Fixnum idx = Fixnum.cast(ithr.getenv(0, 0), this);
		if (idx.fixnum < 0)
			throw new NekoArcException("negative vector index");
		if (idx.fixnum >= this.len())
			throw new NekoArcException("vector index out of range");
		return(vec[(int)idx.fixnum]);
	}

	@Override
	public Iterator<ArcObject> iterator() {
		return(new Iterator<ArcObject>() {
			private int idx = 0;
			@Override
			public boolean hasNext() {
				return(idx < Vector.this.len());
			}

			@Override
			public ArcObject next() {
				return(Vector.this.index(idx++));
			}
		});
	}

	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("cons")) {
			ArcObject list = Nil.NIL;
			for (int i=vec.length-1; i>=0; i--)
				list = new Cons(vec[i], list);
			return(list);
		}

		if (newtype == Symbol.intern("string")) {
			ArcObject str = new AString("");
			for (ArcObject obj : this)
				str = str.add(obj.coerce(newtype, extra));
			return(str);
		}
		return(super.coerce(newtype, extra));
	}
}

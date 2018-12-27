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

import java.util.Iterator;

/**
 * Cons cells
 */
public class Cons extends ArcObject implements Iterable<ArcObject> {
	public static final ArcObject TYPE = Symbol.intern("cons");
	private ArcObject car;
	private ArcObject cdr;

	/**
	 * Create an empty cons cell with car and cdr both nil
	 */
	public Cons() {
	    this.car = this.cdr = Nil.NIL;
	}

	/**
	 * Create a cons cell.
	 * @param car The value of the car
	 * @param cdr The value of the cdr
	 */
	public Cons(ArcObject car, ArcObject cdr) {
		this.car = car;
		this.cdr = cdr;
	}

	@Override
	public ArcObject type() {
		return(TYPE);
	}

	/**
	 * The car of this cons cell
	 * @return Return the car of the cons ceell
	 */
	@Override
	public ArcObject car() {
		return(car);
	}

	/**
	 * The cdr of this cons cell
	 * @return the cdr
	 */
	@Override
	public ArcObject cdr() {
		return(cdr);
	}

	/**
	 * Set the car of the cons cell
	 * @param ncar The new value of the cell's car
	 * @return the car
	 */
	@Override
	public ArcObject scar(ArcObject ncar) {
		return(this.car = ncar);
	}

	/**
	 * Set the cdr of the cons cell
	 * @param ncdr the new value of the cell's cdr
	 * @return the new value
	 */
	@Override
	public ArcObject scdr(ArcObject ncdr) {
		return(this.cdr = ncdr);
	}

	/**
	 * Set a reference into the cons cell. It treas the cons cell as a list, and will
	 * iterate down the cdr chain as many times as the index
	 * @param value The new value to assign to the index
	 * @param idx The index into the list
	 * @return The value that was set.
	 */
	@Override
	public ArcObject sref(ArcObject value, ArcObject idx) {
		Fixnum index = Fixnum.cast(idx, this);
		nth(index.fixnum).scar(value);
		return(value);
	}

	@Override
    public long len() {
	    // FIXME: Handle cyclic lists somehow
        if (cdr instanceof Cons)
            return(1 + cdr.len());
        throw new NekoArcException("cannot get length of improper list");
    }

    @SuppressWarnings("serial")
	private static class OOB extends RuntimeException {
	}

	/**
	 * Get the nth item in a list of cons cells
	 * @param idx The index
	 * @return The item at that index if it is within the list
	 */
	public Cons nth(long idx) {
		try {
			return((Cons)nth(this, idx));
		} catch (OOB oob) {
			throw new NekoArcException("Error: index " + idx + " too large for list " + this);
		}
	}

	/**
	 * Get the nth item from an object, assuming it to have a valid cdr
	 * @param c The object
	 * @param idx the index
	 * @return The value at that index, if available
	 */
	private static ArcObject nth(ArcObject c, long idx) {
		while (idx > 0) {
			if (c.cdr() instanceof Nil)
				throw new OOB();
			c = c.cdr();
			idx--;
		}
		return(c);
	}

	/**
	 * Get an iterator for the list of cons cells
	 * @return The iterator
	 */
    @Override
    public Iterator<ArcObject> iterator() {
        final ArcObject self = this;
        return(new Iterator<ArcObject>() {

            private ArcObject obj = self;

            @Override
            public boolean hasNext() {
                return(obj instanceof Cons && !Nil.NIL.is(obj));
            }

            @Override
            public ArcObject next() {
                if (obj.cdr() instanceof Cons) {
                    ArcObject c = obj.car();
                    obj = obj.cdr();
                    return(c);
                }
                ArcObject t = obj;
                obj = Nil.NIL;
                return(t);
            }
        });
    }

	/**
	 * Is the other object equal to this one?
	 * @param other the object to compare with
	 * @return true if they are structurally equal
	 */
	@Override
	public boolean iso(ArcObject other) {
	    if (this.is(other))
	        return(true);
		// FIXME: This will recurse forever if the conses have any cycles!
        if (!(other instanceof Cons))
            return(false);
        Cons o = (Cons)other;
        return(car.iso(o.car()) && cdr.iso(o.cdr()));
	}


	/**
	 * Get a string representation of the cons cell as a list.
	 * @return The string representation of the conses
	 */
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        // FIXME: recursion might go on forever if there are any cycles
        Iterator<ArcObject> iter = this.iterator();
        while (iter.hasNext()) {
            ArcObject t = iter.next();
            if (t instanceof Cons && !Nil.NIL.is(t) && !iter.hasNext()) {
                sb.append(t.car().toString());
                sb.append(" . ");
                sb.append(t.cdr().toString());
            } else
                sb.append(t.toString());
            if (iter.hasNext())
                sb.append(" ");
        }
        sb.append(")");
        return(sb.toString());
    }

	/**
	 * Required args to apply a list
	 * @return Always 1
	 */
	@Override
	public int requiredArgs() {
		return(1);
	}

	/**
	 * Do a functional application of a list of conses. This will take only one argument, which
	 * must be something that can be cast to a fixnum (n), and return the nth index into the list of
	 * conses.
	 * @param ithr The invocation thread.
	 * @return The value at the nth-index of the list of conses
	 */
	@Override
	public ArcObject invoke(InvokeThread ithr) {
		Fixnum idx = Fixnum.cast(ithr.getenv(0, 0), this);
		return(this.nth(idx.fixnum).car());
	}

	/**
	 * Coerce the cons to some other type. The only valid coercions are to strings and vectors. All
	 * other attempts will result in an error
	 * @param newtype The new type to convert to
	 * @param extra additional parameters for the conversion, if any
	 * @return The representation of the cons to the new type.
	 */
	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("string")) {
			ArcObject sum = car.coerce(newtype, extra);
			if (cdr instanceof Cons)
				return(sum.add(cdr.coerce(newtype, extra)));
			throw new NekoArcException("cannot coerce improper list to " + newtype);
		}

		if (newtype == Symbol.intern("vector")) {
			long len = this.len();
			Vector vec = new Vector((int) len);
			int i=0;
			for (ArcObject obj : this)
				vec.setIndex(i++, obj);
			return(vec);
		}
		return(super.coerce(newtype, extra));
	}
}


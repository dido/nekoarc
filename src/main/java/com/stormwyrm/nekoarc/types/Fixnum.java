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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.util.LongMap;

public class Fixnum extends Numeric implements Orderable {
	public final ArcObject TYPE = Symbol.intern("fixnum");
	public final long fixnum;
	private static final LongMap<WeakReference<Fixnum>> table = new LongMap<>();
	private static final ReferenceQueue<Fixnum> rq = new ReferenceQueue<>();
	public static final Fixnum ZERO = get(0);
	public static final Fixnum ONE = get(1);
	public static final Fixnum TEN = get(10);

	/**
	 * Private constructor. Should never be directly invoked, so that there is only one Fixnum object for
	 * every integer.
	 * @param x The integer to make a fixnum out of
	 */
	private Fixnum(long x)
	{
		fixnum = x;
	}

	/**
	 * Get a Fixnum instance for a particular Java long. There should be only one Fixnum object for any
	 * integer, and this method looks up an internal table with weak references for each Fixnum in use.
	 * @param x a long int to get
	 * @return the Fixnum instance corresponding to it
	 */
	public static Fixnum get(long x) {
		Fixnum f;
		if (table.containsKey(x)) {
			WeakReference<Fixnum> wrf = table.get(x);
			f = wrf.get();
			if (f != null)
				return(f);
		}
		f = new Fixnum(x);
		table.put(x, new WeakReference<>(f, rq));
		return(f);
	}

	@Override
	public ArcObject type()
	{
		return(TYPE);
	}

	/**
	 * Cast something to a fixnum. As of now casts can only be performed on Fixnum (trivial) or
	 * Flonum. All other casts are invalid and will throw an exception.
	 * @param arg The object to cast to a
	 * @param caller The object performing the cast
	 * @return The object cast to a fixnum, if this was possible
	 */
	public static Fixnum cast(ArcObject arg, ArcObject caller) {
		if (arg instanceof Flonum) {
			return(Fixnum.get((long)((Flonum)arg).flonum));
		} else if (arg instanceof Fixnum) {
			return((Fixnum)arg);
		}
		throw new NekoArcException("Wrong argument type, caller " + caller + " expected a Fixnum, got " + arg);
	}

	/**
	 * Add ae to this. If ae is a flonum, it will cast this to a flonum and the addition will proceed that way.
	 * Otherwise the other object is cast to a Fixnum and added as such.
	 * @param ae The addend
	 * @return The sum, if this was possible
	 */
	@Override
	public ArcObject add(ArcObject ae) {
		if (ae instanceof Flonum) {
			Flonum fnum = Flonum.cast(this, this);
			return(fnum.add(ae));
		}
		Fixnum addend = Fixnum.cast(ae, this);
		return(Fixnum.get(this.fixnum + addend.fixnum));
	}

	/**
	 * Is this an exact quantity?
	 * @return Always true
	 */
    @Override
    public boolean exactP() {
        return(true);
    }

	/**
	 * Negate the fixnum
	 * @return A fixnum which is the negative of this
	 */
	@Override
	public Numeric negate() {
		return(Fixnum.get(-this.fixnum));
	}

	/**
	 * Multiply factor to this. If factor is a flonum, it will cast this to a flonum and the multiplication will
	 * proceed that way. Otherwise the other object is cast to a Fixnum and multiplied as such. Note that fixnum
	 * multiplication can overflow.
	 * @param factor the other factor
	 * @return The product, if this was possible
	 */
	@Override
	public Numeric mul(Numeric factor) {
		if (factor instanceof Flonum) {
			Flonum self = Flonum.cast(this, this);
			return(self.mul(factor));
		}
		// note: multiplying large fixnums may have unexpected results!
		Fixnum ffactor = Fixnum.cast(factor, this);
		return(Fixnum.get(this.fixnum * ffactor.fixnum));
	}

	/**
	 * Divide divisor to this. If divisor is a flonum, it will cast this to a flonum and the division will proceed
	 * that way. Otherwise, the other object is cast to Flonum and divided as such.
	 *
	 * @param divisor the divisor
	 * @return The quotient, if this was possible
	 */
	@Override
	public Numeric div(Numeric divisor) {
		if (divisor instanceof Flonum) {
			Flonum self = Flonum.cast(this, this);
			return(self.div(divisor));
		}
		Fixnum fdivisor = Fixnum.cast(divisor, this);
		return(Fixnum.get(this.fixnum / fdivisor.fixnum));
	}

	/**
	 * Get string representation of fixnum
	 * @return the string representation of the fixnum
	 */
	@Override
	public String toString() {
		return(String.valueOf(fixnum));
	}

	/**
	 * Is x less than a fixnum? This is only valid if the other object is a fixnum or a flonum. Else there will be
	 * an error.
	 * @param x The object to compare
	 * @return true if this is less than x
	 */
	@Override
	public boolean lessThan(ArcObject x) {
	    if (x instanceof Flonum)
	        return((double)this.fixnum < ((Flonum)x).flonum);
		Fixnum f = Fixnum.cast(x, this);
		return(this.fixnum < f.fixnum);
	}

	/**
	 * Coerce this to a new type.  The only valid conversions as of now are to flonum, string (with an optional
	 * base parameter), and rune (treating the fixnum as a Unicode code point).
	 * @param newtype The new type to convert to
	 * @param params Optionally the base if the conversion was to a string, ignored otherwise
	 * @return An object of newtype converted from this if that was possible.
	 */
	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject params) {
        if (newtype == Symbol.intern("flonum"))
            return(Flonum.cast(this, this));
        if (newtype == Symbol.intern("string")) {
            int base=10;
            if (!Nil.NIL.is(params) && params.car() instanceof Fixnum)
                base = (int)((Fixnum)params.car()).fixnum;
            return(new AString(Long.toString(this.fixnum, base)));
        }
        if (newtype == Symbol.intern("rune"))
            return(Rune.get((int) this.fixnum));
        return(super.coerce(newtype, params));
	}
}

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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.util.LongMap;

public class Fixnum extends Numeric implements Orderable
{
	public final ArcObject TYPE = Symbol.intern("fixnum");
	public final long fixnum;
	private static final LongMap<WeakReference<Fixnum>> table = new LongMap<>();
	private static final ReferenceQueue<Fixnum> rq = new ReferenceQueue<>();
	public static final Fixnum ZERO = get(0);
	public static final Fixnum ONE = get(1);
	public static final Fixnum TEN = get(10);

	private Fixnum(long x)
	{
		fixnum = x;
	}

	public static Fixnum get(long x)
	{
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

	public static Fixnum cast(ArcObject arg, ArcObject caller)
	{
		if (arg instanceof Flonum) {
			return(Fixnum.get((long)((Flonum)arg).flonum));
		} else if (arg instanceof Fixnum) {
			return((Fixnum)arg);
		}
		throw new NekoArcException("Wrong argument type, caller " + caller + " expected a Fixnum, got " + arg);
	}

	@Override
	public ArcObject add(ArcObject ae)
	{
		if (ae instanceof Flonum) {
			Flonum fnum = Flonum.cast(this, this);
			return(fnum.add(ae));
		}
		Fixnum addend = Fixnum.cast(ae, this);
		return(Fixnum.get(this.fixnum + addend.fixnum));
	}

    @Override
    public boolean exactP() {
        return(true);
    }

    @Override
	public Numeric negate()
	{
		return(Fixnum.get(-this.fixnum));
	}

	@Override
	public Numeric mul(Numeric factor)
	{
		if (factor instanceof Flonum) {
			Flonum self = Flonum.cast(this, this);
			return(self.mul(factor));
		}
		// note: multiplying large fixnums may have unexpected results!
		Fixnum ffactor = Fixnum.cast(factor, this);
		return(Fixnum.get(this.fixnum * ffactor.fixnum));
	}

	@Override
	public Numeric div(Numeric divisor)
	{
		if (divisor instanceof Flonum) {
			Flonum self = Flonum.cast(this, this);
			return(self.div(divisor));
		}
		Fixnum fdivisor = Fixnum.cast(divisor, this);
		return(Fixnum.get(this.fixnum / fdivisor.fixnum));
	}

	@Override
	public String toString()
	{
		return(String.valueOf(fixnum));
	}

	@Override
	public boolean lessThan(ArcObject x) {
	    if (x instanceof Flonum)
	        return((double)this.fixnum < ((Flonum)x).flonum);
		Fixnum f = Fixnum.cast(x, this);
		return(this.fixnum < f.fixnum);
	}

	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject params) {
        if (newtype == Symbol.intern("fixnum"))
            return(this);
        if (newtype == Symbol.intern("flonum"))
            return(Flonum.cast(this, this));
        if (newtype == Symbol.intern("string"))
            return(new AString(this.toString()));
        if (newtype == Symbol.intern("rune"))
            return(Rune.get((int) this.fixnum));
        return(super.coerce(newtype, params));
	}
}

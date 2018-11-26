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

import com.stormwyrm.nekoarc.NekoArcException;

public class Flonum extends Numeric implements Orderable
{
	public static final ArcObject TYPE = Symbol.intern("flonum");
	public double flonum;
	
	public Flonum(double flonum)
	{
		this.flonum = flonum;
	}

	@Override
	public ArcObject type()
	{
		return(TYPE);
	}

    /**
     * Cast something into a flonum. The only valid cast right now is for a fixnum.
     * @param arg the object to cast to flonum
     * @param caller the object trying to make the conversion
     * @return the object cast to flonum, if this is valid
     */
	public static Flonum cast(ArcObject arg, ArcObject caller)
	{
		if (arg instanceof Fixnum) {
			return(new Flonum((double)((Fixnum) arg).fixnum));
		} else if (arg instanceof Flonum) {
			return((Flonum)arg);
		}
		throw new NekoArcException("Wrong argument type, caller " + caller + " expected a Fixnum, got " + arg);
	}

    /**
     * Add something to a flonum. The type must be the sort which can be coerced to a flonum. At the moment, other than
     * another flonum, only a fixnum may be added to a flonum. Anything else is an erro.
     * @param ae the addend
     * @return the sum, if the type of ae can be coerced to a flonum
     */
	@Override
	public ArcObject add(ArcObject ae)
	{
		Flonum addend = Flonum.cast(ae, this);
		return(new Flonum(this.flonum + addend.flonum));
	}

    /**
     * Multiply something to a flonum. Again the thing to be multiplied is cast to a flonum if this is possible.
     * @param f the thing to be multiplied
     * @return their product
     */
	public Flonum mul(Numeric f)
	{
		Flonum factor = Flonum.cast(f, this);
		return(new Flonum(this.flonum * factor.flonum));
	}


    /**
     * Divide the flonum by d, suitably cast to flonum.
     * @param d the divident
     * @return the quotient
     */
	public Flonum div(Numeric d)
	{
		Flonum divisor = Flonum.cast(d, this);
		return(new Flonum(this.flonum / divisor.flonum));
	}

    /**
     * Negate the flonum
     * @return A new flonum that is the original's negation
     */
	@Override
	public Numeric negate()
	{
		return(new Flonum(-this.flonum));
	}

    /**
     * Convert a flonum to a string
     * @return the string representation of the flonum
     */
	@Override
	public String toString()
	{
		return(String.valueOf(flonum));
	}

    /**
     * Compare something to a flonum.
     * @param other The object to compare
     * @return true if the flonums are identical
     */
	@Override
	public boolean is(ArcObject other)
	{
		return(this == other || ((other instanceof Flonum) && flonum == (((Flonum)other).flonum)));
	}


    /**
     * See if the flonum is less than something. It will be cast to flonum before comparing.
     * @param x the thing to compare the flonum against
     * @return true if the flonum is less than
     */
	@Override
	public boolean lessThan(ArcObject x) {
		Flonum f = Flonum.cast(x, this);
		return(this.flonum < f.flonum);
	}

    /**
     * Coerce a flonum to the new type
     * @param newtype The new type to convert to
     * @param extra ignored
     * @return the flonum suitably converted
     */
    @Override
    public ArcObject coerce(ArcObject newtype, ArcObject extra) {
        if (newtype == Symbol.intern("flonum"))
            return(this);
        if (newtype == Symbol.intern("fixnum"))
            return(Fixnum.cast(this, this));
        if (newtype == Symbol.intern("string"))
            return(new AString(this.toString()));
        return(super.coerce(newtype, extra));
    }
}

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

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.ciel.CAsm;

public class AString extends ArcObject
{
	public static final ArcObject TYPE = Symbol.intern("string");
	public final StringBuffer string;

	public AString(String str) {
		this.string = new StringBuffer(str);
	}

	public AString(long len, Rune r) {
		this.string = new StringBuffer((int)len);
		while (len-- > 0)
			string.appendCodePoint(r.rune);
	}

	@Override
	public ArcObject type()
	{
		return(TYPE);
	}

	@Override
	public ArcObject add(ArcObject ae) {
		return(new AString(this.string + ae.toString()));
	}

	@Override
	public long len() {
		return(string.length());
	}

	@Override
	public String toString()
	{
		return(string.toString());
	}

	@Override
	public boolean is(ArcObject other) {
		if (this == other)
			return(true);
		return((other instanceof AString) && (this.toString().compareTo(other.toString())) == 0);
	}

	@Override
	public int requiredArgs()
	{
		return(1);
	}

	@Override
	public ArcObject sref(ArcObject value, ArcObject index) {
		Rune r = (Rune)value;
		int idx = (int)Fixnum.cast(index, this).fixnum;
		this.string.replace(idx, idx+1, String.format("%c", r.rune));
		return(value);
	}

	@Override
	public ArcObject invoke(InvokeThread ithr) {
		Fixnum idx = Fixnum.cast(ithr.getenv(0, 0), this);
		if (idx.fixnum < 0)
			throw new NekoArcException("negative string index");
		if (idx.fixnum >= string.length())
			throw new NekoArcException("string index out of range");
		int c = string.codePointAt((int) idx.fixnum);
		return(Rune.get(c));
	}

    @Override
    public ArcObject coerce(ArcObject newtype, ArcObject extra) {
        if (newtype == Symbol.intern("fixnum")) {
            try {
                int radix = 10;
                if (!Nil.NIL.is(extra))
                    radix = (int)Fixnum.cast(extra.car(), this).fixnum;
                return(Fixnum.get(Long.parseLong(this.toString(), radix)));
            } catch (NumberFormatException e) {
                // do nothing, this will automatically fall through below to report the error
            }
        }

        if (newtype == Symbol.intern("flonum")) {
        	try {
        		return(new Flonum(Double.parseDouble(this.toString())));
			} catch (NumberFormatException e) {
        		// do nothing, this will automatically fall through below to report the error
			}
		}

        if (newtype == Symbol.intern("sym"))
        	return(Symbol.intern(this.toString()));

		if (newtype == Symbol.intern("cons")) {
			ArcObject list = Nil.NIL;
			for (int i=string.length()-1; i>=0; i--)
				list = new Cons(Rune.get(string.codePointAt(i)), list);
			return(list);
		}

		if (newtype == Symbol.intern("vector")) {
			Vector vec = new Vector(string.length());
			for (int i=0; i<string.length(); i++)
				vec.setIndex(i, Rune.get(string.codePointAt(i)));
			return(vec);
		}

        return super.coerce(newtype, extra);
    }

	@Override
	public void marshal(OutputPort p) {
		CAsm.GSTR.emit(p);
		CAsm.writeString(p, string.toString());
	}
}

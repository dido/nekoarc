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

import java.lang.ref.WeakReference;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.util.LongMap;
import com.stormwyrm.nekoarc.util.MurmurHash;

/**
 * Symbol type.
 */
public class Symbol extends Atom {
	public final String symbol;
	private static final LongMap<WeakReference<Symbol>> symtable = new LongMap<>();
	public static final ArcObject TYPE = Symbol.intern("sym");

	private Symbol(String s)
	{
		symbol = s;
	}

	private static long hash(String s) {
//		return((long)s.hashCode());
		return(MurmurHash.hash(s));
	}

	/**
	 * Intern a string into the symbol table.
	 * @param s a string to intern
	 * @return the Symbol representing the string.
	 */
	public static synchronized ArcObject intern(String s) {
		Symbol sym;

		if (s.equals("t"))
			return(True.T);
		if (s.equals("nil"))
			return(Nil.NIL);

		long hc = hash(s);
		if (symtable.containsKey(hc)) {
			WeakReference<Symbol> wref = symtable.get(hc);
			sym = wref.get();
			if (sym != null)
				return(sym);
		}
		sym = new Symbol(s);
		symtable.put(hc, new WeakReference<>(sym));
		return(sym);
	}

	@Override
	public ArcObject type() {
		return(TYPE);
	}

	@Override
	public String toString() {
		return(this.symbol);
	}

	/**
	 * Coerce a symbol to some other type. It is only valid to convert strings to symbols.
	 * @param newtype The new type to convert to
	 * @param extra additional parameters for the conversion, if any
	 * @return the symbol coerced to the new type.
	 */
	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("string"))
			return(new AString(this.toString()));
		return(super.coerce(newtype, extra));
	}
}

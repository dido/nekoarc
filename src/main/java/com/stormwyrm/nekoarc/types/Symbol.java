package com.stormwyrm.nekoarc.types;

import java.lang.ref.WeakReference;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.util.LongMap;
import com.stormwyrm.nekoarc.util.MurmurHash;

public class Symbol extends Atom
{
	public final String symbol;
	private static final LongMap<WeakReference<Symbol>> symtable = new LongMap<>();
	public static final ArcObject TYPE = Symbol.intern("sym");

	private Symbol(String s)
	{
		symbol = s;
	}

	private static long hash(String s)
	{
//		return((long)s.hashCode());
		return(MurmurHash.hash(s));
	}

	public long hash()
	{
		return(hash(this.symbol));
	}

	public static ArcObject intern(String s)
	{
		Symbol sym;
		long hc = hash(s);

		if (s.equals("t"))
			return(True.T);
		if (s.equals("nil"))
			return(Nil.NIL);

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
	public ArcObject type()
	{
		return(TYPE);
	}

	@Override
	public String toString()
	{
		return(this.symbol);
	}

	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("string"))
			return(new AString(this.toString()));
		return(super.coerce(newtype, extra));
	}
}

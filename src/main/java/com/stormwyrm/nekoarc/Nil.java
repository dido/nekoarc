package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Symbol;

public class Nil extends Cons
{
	public static final ArcObject TYPE = Symbol.TYPE;
	public static final Nil NIL = new Nil("nil");
	public static final Nil EMPTY_LIST = new Nil("()");
	private String rep;

	private Nil(String rep)
	{
		this.rep = rep;
	}

	public String toString()
	{
		return(rep);
	}

	@Override
	public long len() {
        return (0);
    }

    @Override
	public boolean is(ArcObject other)
	{
		return(this == other || (other instanceof Nil));
	}

	@Override
	public boolean iso(ArcObject object) {
		return(is(object));
	}

	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("string"))
			return(new AString(""));

		return(super.coerce(newtype, extra));
	}
}

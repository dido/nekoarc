package com.stormwyrm.nekoarc;

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
	public boolean is(ArcObject other)
	{
		return(this == other || (other instanceof Nil));
	}
}

package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;

public class True extends ArcObject
{
	public static final ArcObject TYPE = Symbol.TYPE;
	public static final True T = new True("t");
	private final String rep;

	private True(String rep)
	{
		this.rep = rep;
	}
	
	public ArcObject type()
	{
		return(Symbol.TYPE);
	}

	public String toString()
	{
		return(rep);
	}

	@Override
	public boolean is(ArcObject other)
	{
		return(this == other || (other instanceof True) || (other instanceof Symbol && ((Symbol)other).symbol == this.rep));
	}
}

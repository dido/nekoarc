package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;

public class Unbound extends ArcObject
{
	public static final ArcObject TYPE = Symbol.intern("unbound");
	public static final Unbound UNBOUND = new Unbound();

	private Unbound()
	{
	}

	@Override
	public ArcObject type()
	{
		return(TYPE);
	}

	@Override
	public boolean is(ArcObject other)
	{
		return(this == other || (other instanceof Unbound));
	}
}

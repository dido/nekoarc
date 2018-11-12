package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;

public abstract class Builtin extends ArcObject
{
	public static final Symbol TYPE = (Symbol) Symbol.intern("fn");
	private final String name;
	private final int rargs, eargs, oargs;
	private final boolean variadic;

	protected Builtin(String name, int req, int opt, int extra, boolean va)
	{
		this.name = name;
		rargs = req;
		eargs = extra;
		oargs = opt;
		variadic = va;
	}

	protected Builtin(String name, int req)
	{
		this(name, req, 0, 0, false);
	}

	public String getName()
	{
		return(name);
	}

	@Override
	public int requiredArgs()
	{
		return(rargs);
	}

	public int optionalArgs()
	{
		return(oargs);
	}

	public int extraArgs()
	{
		return(eargs);
	}

	public boolean variadicP()
	{
		return(variadic);
	}

	public abstract ArcObject invoke(InvokeThread vm);

	public ArcObject type()
	{
		return(TYPE);
	}

	@Override
	public String toString() {
		return("#<procedure:" + name +">");
	}
}

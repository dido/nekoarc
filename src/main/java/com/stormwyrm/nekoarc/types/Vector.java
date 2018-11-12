package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;

public class Vector extends ArcObject
{
	public static final ArcObject TYPE = Symbol.intern("vector");
	private ArcObject[] vec;

	public Vector(int length)
	{
		vec = new ArcObject[length];
	}

	public ArcObject index(int i)
	{
		return(vec[i]);
	}

	public ArcObject setIndex(int i, ArcObject val)
	{
		return(vec[i] = val);
	}

	@Override
	public long len()
	{
		return(vec.length);
	}

	@Override
	public ArcObject type()
	{
		return(TYPE);
	}

	@Override
	public String toString()
	{
		return("<" + type().toString() + ">");
	}

	@Override
	public int requiredArgs()
	{
		return(1);
	}

	@Override
	public ArcObject invoke(InvokeThread thr)
	{
		Fixnum idx = Fixnum.cast(thr.getenv(0, 0), this);
		if (idx.fixnum < 0)
			throw new NekoArcException("negative vector index");
		if (idx.fixnum >= this.len())
			throw new NekoArcException("vector index out of range");
		return(vec[(int)idx.fixnum]);
	}

}

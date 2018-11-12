package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;

public class AString extends ArcObject
{
	public static final ArcObject TYPE = Symbol.intern("string");
	public final String string;

	public AString(String str)
	{
		this.string = str;
	}

	public AString(long len, Rune r) {
		StringBuilder sb = new StringBuilder();
		while (len-- > 0)
			sb.appendCodePoint(r.rune);
		this.string = sb.toString();
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
		return(string);
	}

	@Override
	public boolean is(ArcObject other)
	{
		return(this == other || ((other instanceof AString) && string.equals(((AString)other).string)));
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
			throw new NekoArcException("negative string index");
		if (idx.fixnum >= string.length())
			throw new NekoArcException("string index out of range");
		int c = string.codePointAt((int) idx.fixnum);
		return(Rune.get(c));
	}
}

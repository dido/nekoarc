package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;

import java.util.Iterator;

public class Vector extends ArcObject implements Iterable<ArcObject>
{
	public static final ArcObject TYPE = Symbol.intern("vector");
	private ArcObject[] vec;

	public Vector(int length)
	{
		vec = new ArcObject[length];
	}

	public Vector(ArcObject... data) {
		this(data.length);
		System.arraycopy(data, 0, vec, 0, data.length);
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
	public String toString() {
	    StringBuilder sb = new StringBuilder("[");
	    // FIXME: This is again recursive, and requires cycle detection
        int i=0;
        while (i<vec.length) {
            sb.append(vec[i].toString());
            if (i<vec.length-1)
                sb.append(" ");
            i++;
        }
        sb.append("]");
        return(sb.toString());
	}

    @Override
    public boolean iso(ArcObject other) {
        // FIXME: also recursive
        if (!(other instanceof Vector))
            return(false);
        Vector v2 = (Vector)other;
        if (v2.len() != this.len())
            return(false);
        for (int i=0; i<this.len(); i++) {
            if (!v2.index(i).iso(this.index(i)))
                return(false);
        }
        return(true);
    }

    @Override
	public int requiredArgs()
	{
		return(1);
	}

	@Override
    public ArcObject sref(ArcObject value, ArcObject index) {
	    return(this.setIndex((int) Fixnum.cast(index, this).fixnum, value));
    }

    @Override
	public ArcObject invoke(InvokeThread ithr)
	{
		Fixnum idx = Fixnum.cast(ithr.getenv(0, 0), this);
		if (idx.fixnum < 0)
			throw new NekoArcException("negative vector index");
		if (idx.fixnum >= this.len())
			throw new NekoArcException("vector index out of range");
		return(vec[(int)idx.fixnum]);
	}

	@Override
	public Iterator<ArcObject> iterator() {
		return(new Iterator<ArcObject>() {
			private int idx = 0;
			@Override
			public boolean hasNext() {
				return(idx < Vector.this.len());
			}

			@Override
			public ArcObject next() {
				return(Vector.this.index(idx++));
			}
		});
	}

	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("cons")) {
			ArcObject list = Nil.NIL;
			for (int i=vec.length-1; i>=0; i--)
				list = new Cons(vec[i], list);
			return(list);
		}

		if (newtype == Symbol.intern("string")) {
			ArcObject str = new AString("");
			for (ArcObject obj : this)
				str = str.add(obj.coerce(newtype, extra));
			return(str);
		}
		return(super.coerce(newtype, extra));
	}
}

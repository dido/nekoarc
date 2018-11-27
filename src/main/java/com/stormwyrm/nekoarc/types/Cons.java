package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;

import java.util.Iterator;

public class Cons extends ArcObject implements Iterable<ArcObject>
{
	public static final ArcObject TYPE = Symbol.intern("cons");
	private ArcObject car;
	private ArcObject cdr;

	public Cons()
	{
	    this.car = this.cdr = Nil.NIL;
	}

	public Cons(ArcObject car, ArcObject cdr)
	{
		this.car = car;
		this.cdr = cdr;
	}

	@Override
	public ArcObject type()
	{
		return(TYPE);
	}

	@Override
	public ArcObject car()
	{
		return(car);
	}

	@Override
	public ArcObject cdr()
	{
		return(cdr);
	}

	@Override
	public ArcObject scar(ArcObject ncar)
	{
		this.car = ncar;
		return(ncar);
	}

	@Override
	public ArcObject scdr(ArcObject ncdr)
	{
		this.cdr = ncdr;
		return(ncdr);
	}
	
	@Override
	public ArcObject sref(ArcObject value, ArcObject idx)
	{
		Fixnum index = Fixnum.cast(idx, this);
		return(nth(index.fixnum).scar(value));
	}

	@Override
    public long len() {
	    // FIXME: Handle cyclic lists somehow
        if (cdr instanceof Cons)
            return(1 + cdr.len());
        throw new NekoArcException("cannot get length of improper list");
    }

    @SuppressWarnings("serial")
	private static class OOB extends RuntimeException {
	}

	public Cons nth(long idx)
	{
		try {
			return((Cons)nth(this, idx));
		} catch (OOB oob) {
			throw new NekoArcException("Error: index " + idx + " too large for list " + this);
		}
	}

	private static ArcObject nth(ArcObject c, long idx)
	{
		while (idx > 0) {
			if (c.cdr() instanceof Nil)
				throw new OOB();
			c = c.cdr();
			idx--;
		}
		return(c);
	}

    @Override
    public Iterator<ArcObject> iterator() {
        final ArcObject self = this;
        return(new Iterator<ArcObject>() {

            private ArcObject obj = self;

            @Override
            public boolean hasNext() {
                return(obj instanceof Cons && !Nil.NIL.is(obj));
            }

            @Override
            public ArcObject next() {
                if (obj.cdr() instanceof Cons) {
                    ArcObject c = obj.car();
                    obj = obj.cdr();
                    return(c);
                }
                ArcObject t = obj;
                obj = Nil.NIL;
                return(t);
            }
        });
    }

	@Override
	public boolean iso(ArcObject other) {
	    if (this.is(other))
	        return(true);
		// FIXME: This will recurse forever if the conses have any cycles!
        if (!(other instanceof Cons))
            return(false);
        Cons o = (Cons)other;
        return(car.iso(o.car()) && cdr.iso(o.cdr()));
	}


    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        // FIXME: recursion might go on forever if there are any cycles
        Iterator<ArcObject> iter = this.iterator();
        while (iter.hasNext()) {
            ArcObject t = iter.next();
            if (t instanceof Cons && !Nil.NIL.is(t) && !iter.hasNext()) {
                sb.append(t.car().toString());
                sb.append(" . ");
                sb.append(t.cdr().toString());
            } else
                sb.append(t.toString());
            if (iter.hasNext())
                sb.append(" ");
        }
        sb.append(")");
        return(sb.toString());
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
		return(this.nth(idx.fixnum).car());
	}

	@Override
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		if (newtype == Symbol.intern("string")) {
			ArcObject sum = car.coerce(newtype, extra);
			if (cdr instanceof Cons)
				return(sum.add(cdr.coerce(newtype, extra)));
			throw new NekoArcException("cannot coerce improper list to " + newtype);
		}

		if (newtype == Symbol.intern("vector")) {
			long len = this.len();
			Vector vec = new Vector((int) len);
			int i=0;
			for (ArcObject obj : this)
				vec.setIndex(i++, obj);
			return(vec);
		}
		return(super.coerce(newtype, extra));
	}
}


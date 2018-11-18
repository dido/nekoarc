package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.util.CallSync;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public abstract class ArcObject implements Callable
{
	private final CallSync caller = new CallSync();

	public ArcObject car()
	{
		throw new NekoArcException("Can't take car of " + this.type());
	}

	public ArcObject cdr()
	{
		throw new NekoArcException("Can't take cdr of " + this.type());
	}

	public ArcObject scar(ArcObject ncar)
	{
		throw new NekoArcException("Can't set car of " + this.type());
	}

	public ArcObject scdr(ArcObject ncar)
	{
		throw new NekoArcException("Can't set cdr of " + this.type());
	}

	
	public ArcObject sref(ArcObject value, ArcObject index)
	{
		throw new NekoArcException("Can't sref " + this + "(a " + this.type() + "), other args were " + value + ", " + index);
	}

	public ArcObject add(ArcObject other)
	{
		throw new NekoArcException("add not implemented for " + this.type() + " " + this);
	}

	public long len()
	{
		throw new NekoArcException("len: expects one string, vector, list, or hash argument, cannot take length of " + this + " (" + this.type() + ")");
	}

	public abstract ArcObject type();

	public ArcObject rep() {
		return(this);
	}

	public int requiredArgs()
	{
		throw new NekoArcException("Cannot invoke object of type " + type());
	}

	public int optionalArgs()
	{
		return(0);
	}

	public int extraArgs()
	{
		return(0);
	}

	public boolean variadicP()
	{
		return(false);
	}

	public boolean exactP() { return(false); }

	/** The basic apply. This should normally not be overridden. Only Closure should
	 * probably override it because it runs completely within the vm. */
	public void apply(VirtualMachine vm, Callable caller)
	{
		int minenv, dsenv, optenv;
		minenv = requiredArgs();
		dsenv = extraArgs();
		optenv = optionalArgs();
		if (variadicP()) {
			int i;
			vm.argcheck(minenv, -1);
			ArcObject rest = Nil.NIL;
			for (i = vm.argc(); i>(minenv + optenv); i--)
				rest = new Cons(vm.pop(), rest);
			vm.mkenv(i, minenv + optenv - i + dsenv + 1);
			/* store the rest parameter */
			vm.setenv(0, minenv + optenv + dsenv, rest);
		} else {
			vm.argcheck(minenv, minenv + optenv);
			vm.mkenv(vm.argc(), minenv + optenv - vm.argc() + dsenv);
		}

		// Start the invoke thread
		InvokeThread thr = new InvokeThread(vm, caller, this);
		new Thread(thr).start();

		// Suspend the caller's thread until the invoke thread returns
		vm.setAcc(caller.sync().retval());
	}

	public ArcObject invoke(InvokeThread vm)
	{
		throw new NekoArcException("Cannot invoke object of type " + type());
	}

	public String toString()
	{
		throw new NekoArcException("Type " + type() + " has no string representation");
	}

	// default implementation
	public boolean is(ArcObject other)
	{
		return(this == other);
	}

	// default implementation, subclasses must generally redefine unless object equality is the same as structural
    // equality
	public boolean iso(ArcObject other) { return(this.is(other)); }

	@Override
	public CallSync sync()
	{
		return(caller );
	}
}


/*  Copyright (C) 2018 Rafael R. Sevilla

    This file is part of NekoArc

    NekoArc is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 3 of the
    License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

/** The main reason this class exists is that Java is a weak-sauce language that doesn't have closures or true
 *  continuations. We have to emulate them using threads. */
public class InvokeThread extends Thread
{
	private final Callable caller;
	private final ArcObject obj;
	public final VirtualMachine vm;

	public InvokeThread(VirtualMachine v, Callable c, ArcObject o)
	{
		vm = v;
		caller = c;
		obj = o;
	}

	@Override
	public void run()
	{
		// Perform our function's thing
		ArcObject ret = obj.invoke(this);
		// Restore the continuation created by the caller
		vm.restorecont(caller);
		// Return the result to our caller's thread, waking them up
		caller.sync().ret(ret);
		// and this invoke thread's work is ended
	}

	public ArcObject getenv(int i, int j)
	{
		return(vm.getenv(i, j));
	}

    public ArcObject getenv(int j) {
        return(getenv(0, j));
    }

	/** Perform a call to some Arc object. This should prolly work for ANY ArcObject that has a proper invoke method
     *  defined.  If it is a built-in or some function defined in Java, that function will run in its own thread while
     *  the current object's thread is suspended. */
	public ArcObject apply(ArcObject fn, ArcObject...args)
	{
		// First, push all of the arguments to the stack
		for (ArcObject arg : args)
			vm.push(arg);

		// new continuation
		vm.setCont(new JavaContinuation(vm, obj));

		// Apply the function.
		fn.apply(vm, obj);

		return(vm.getAcc());
	}

    public int argc() {
		return(vm.argc());
    }
}

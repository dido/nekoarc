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

import com.stormwyrm.nekoarc.types.AException;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.types.ArcThread;

/**
 * The main reason this class exists is that Java is a weak-sauce language that doesn't have closures or true
 * continuations. We have to emulate them using threads.  When Java code needs to be executed by a virtual machine
 * thread, an InvokeThread is created that runs the code in the ArcObject containing the code (usually a Builtin),
 * while the calling thread is suspended on the Caller sync.
 *
 */
public class InvokeThread extends Thread {
	private final Callable caller;
	private final ArcObject obj;
	public final ArcThread thr;

	/**
	 * Create an invocation thread
	 * @param t the thread for which this InvokeThread is to run under
	 * @param c The caller
	 * @param o The object to be called
	 */
	public InvokeThread(ArcThread t, Callable c, ArcObject o) {
		thr = t;
		caller = c;
		obj = o;
	}

	@Override
	public void run() {
		// Perform our function's thing
		ArcObject ret;
		try {
			ret = obj.invoke(this);
			// Restore the continuation created by the caller
			thr.restorecont(caller);
		} catch (Throwable e) {
			// If the invocation threw something, the ret becomes an AException which wraps the original throwable
			ret = new AException(e);
		}
		// Return the result to our caller's thread, waking them up
		caller.sync().ret(ret);
		// and this invoke thread's work is ended
	}

	public ArcObject getenv(int i, int j) {
		return(thr.getenv(i, j));
	}

    public ArcObject getenv(int j) {
        return(getenv(0, j));
    }

	/**
	 * Perform a call to some Arc object. This should prolly work for ANY ArcObject that has a proper invoke method
	 * defined.  If it is a built-in or some function defined in Java, that function will main in its own thread while
	 * the current object's thread is suspended.
	 * @param fn The object to be applied
	 * @param args the arguments
	 * @return the return value of the application
	 */
	public ArcObject apply(ArcObject fn, ArcObject...args) throws Throwable {
		// First, push all of the arguments to the stack
		thr.setargc(args.length);
		for (ArcObject arg : args)
			thr.push(arg);

		// new continuation
		thr.setCont(new JavaContinuation(thr, obj));

		// Apply the function.
		fn.apply(thr, obj);

		return(thr.getAcc());
	}

    public int argc() {
		return(thr.argc());
    }
}

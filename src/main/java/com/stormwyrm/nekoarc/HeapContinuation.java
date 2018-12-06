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
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.Vector;
import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.types.ArcThread;

/**
 * Heap-based continuations
 */
public class HeapContinuation extends Vector implements Continuation {
	public static final ArcObject TYPE = Symbol.intern("continuation");
	private final ArcObject prevcont;
	private final ArcObject env;
	private final int ipoffset;
	protected final ArcObject myHere;

	public HeapContinuation(int size, ArcObject pcont, ArcObject e, int ip, ArcObject myHere) {
		super(size);
		prevcont = pcont;
		env = e;
		ipoffset = ip;
		this.myHere = myHere;
	}

	/**
	 * Move continuation to stack for restoration
	 * @param thr thread restoring the continuation
	 * @param cc callable
	 */
	@Override
	public void restore(ArcThread thr, Callable cc) {
		int svsize = (int)this.len();

		thr.stackcheck(svsize + 4, "stack overflow while restoring heap continuation");

		int bp = thr.getSP();
		// push the saved stack values back to the stack
		for (int i=0; i<svsize; i++)
			thr.push(index(i));
		// push the saved instruction pointer
		thr.push(Fixnum.get(ipoffset));
		// push the new base pointer
		thr.push(Fixnum.get(bp));
		// push the saved environment
		thr.push(env);
		// push the previous continuation
		thr.push(prevcont);
		thr.setCont(Fixnum.get(thr.getSP()));
		thr.restorecont();
	}

	public static ArcObject fromStackCont(ArcThread vm, ArcObject sc) {
		return(fromStackCont(vm, sc, null));
	}

	/**
	 * Create a new heap continuation from a stack-based continuation.
	 * A stack continuation is a pointer into the stack, such that
	 * [cont-1] -> previous continuation
	 * [cont-2] -> environment
	 * [cont-3] -> base pointer
	 * [cont-4] -> instruction pointer offset
	 * [cont-(5+n)] -> saved stack elements up to saved base pointer position
	 * This function copies all of this relevant information to the a new HeapContinuation so it can later be restored
	 * @param thr The thread whose stack continuation is to be created
	 * @param sc The stack continuation (offset into the stack)
	 * @param deepest deepest
	 * @return the created HeapContinuation
	 */
	public static ArcObject fromStackCont(ArcThread thr, ArcObject sc, int[] deepest) {
		if (sc instanceof HeapContinuation || sc.is(Nil.NIL))
			return(sc);
		int cc = (int)((Fixnum)sc).fixnum;
		// Calculate the size of the actual continuation based on the saved base pointer
		int bp = (int)((Fixnum)thr.stackIndex(cc-3)).fixnum;
		int svsize = cc - bp - 4;
		if (deepest != null && deepest[0] > bp)
			deepest[0] = bp;
		// Turn previous continuation into a heap-based one too
		ArcObject pco = thr.stackIndex(cc-1);
		pco = fromStackCont(thr, pco, deepest);
		ArcObject senv = HeapEnv.fromStackEnv(thr, thr.stackIndex(cc-2), deepest);
		HeapContinuation c = new HeapContinuation(svsize, pco, senv,
				(int)((Fixnum)thr.stackIndex(cc-4)).fixnum, thr.here);

		for (int i=0; i<svsize; i++)
			c.setIndex(i, thr.stackIndex(bp + i));
		return(c);
	}

	@Override
	public int requiredArgs() {
		return(1);
	}

	/**
	 * The application of a continuation -- this will set itself as the current continuation,
	 * ready to be restored just as the invokethread terminates.
	 */
	@Override
	public ArcObject invoke(InvokeThread ithr) {
		// Perform rerooting first before restoring the continuation
		ithr.thr.reroot(ithr, myHere);
		ithr.thr.setCont(this);
		return(ithr.getenv(0, 0));
	}

	/**
	 * Convert to string
	 * @return continuation
	 */
	@Override
	public String toString() {
		return("#<continuation>");
	}
}

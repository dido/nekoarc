/*
 * Copyright (c) 2019 Rafael R. Sevilla
 *
 * This file is part of NekoArc
 *
 * NekoArc is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 *
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

	/**
	 * Create a new heap continuation.
	 * @param size Total size of the continuation
	 * @param pcont Previous continuation
	 * @param e Environment
	 * @param ip Saved instruction pointer
	 */
	public HeapContinuation(int size, ArcObject pcont, ArcObject e, int ip) {
		super(size);
		prevcont = pcont;
		env = e;
		ipoffset = ip;
	}

	/**
	 * Move continuation to stack for restoration
	 * @param thr thread restoring the continuation
	 * @param cc callable
	 */
	@Override
	public void restore(ArcThread thr, Callable cc) {
		int svsize = (int)this.len();

		thr.stackcheck(svsize + ArcThread.CONTSIZE,
                "stack overflow while restoring heap continuation");

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

    /** Offset of previous continuation in stack continuation */
    public static final int PREVCONTOFFSET = 1;
    /** Offset of saved environment register in stack continuation */
    public  static final int ENVOFFSET = 2;
    /** Offset of saved base pointer in stack continuation */
    public static final int BPOFFSET = 3;
    /** Offset of saved instruction pointer in stack continuation */
    public static final int IPOFFSET = 4;
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
	 * @return the created HeapContinuation
	 */
	public static ArcObject fromStackCont(ArcThread thr, ArcObject sc) {
		if (sc instanceof HeapContinuation || sc.is(Nil.NIL))
			return(sc);
		int cc = (int)((Fixnum)sc).fixnum;
		// Calculate the size of the actual continuation based on the saved base pointer
		int bp = (int)((Fixnum)thr.stackIndex(cc-BPOFFSET)).fixnum;
		// Calculate number of stack elements to be saved */
		int svsize = cc - bp - ArcThread.CONTSIZE;
		// Turn previous continuation into a heap-based one too
		ArcObject pco = thr.stackIndex(cc-PREVCONTOFFSET);
		pco = fromStackCont(thr, pco);
		ArcObject senv = HeapEnv.fromStackEnv(thr, thr.stackIndex(cc-ENVOFFSET));
		HeapContinuation c = new HeapContinuation(svsize, pco, senv,
				(int)((Fixnum)thr.stackIndex(cc-IPOFFSET)).fixnum);

		for (int i=0; i<svsize; i++)
			c.setIndex(i, thr.stackIndex(bp + i));
		return(c);
	}

	/**
	 * Required args for applying a heap continuation
	 * @return 1
	 */
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
		ithr.thr.setCont(this);
		return(ithr.getenv(0, 0));
	}

	/**
	 * Convert to string
	 * @return continuation as a string
	 */
	@Override
	public String toString() {
		return("#<continuation:" + this.hashCode() + ">");
	}
}

package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.Vector;
import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.types.ArcThread;

public class HeapContinuation extends Vector implements Continuation
{
	public static final ArcObject TYPE = Symbol.intern("continuation");
	private final ArcObject prevcont;
	private final ArcObject env;
	private final int ipoffset;

	public HeapContinuation(int size, ArcObject pcont, ArcObject e, int ip)
	{
		super(size);
		prevcont = pcont;
		env = e;
		ipoffset = ip;
	}

	/**
	 * Move continuation to stack for restoration
	 * @param thr virtual machine thread
	 * @param cc callable
	 */
	@Override
	public void restore(ArcThread thr, Callable cc)
	{
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

	public static ArcObject fromStackCont(ArcThread vm, ArcObject sc)
	{
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
	 */
	public static ArcObject fromStackCont(ArcThread vm, ArcObject sc, int[] deepest)
	{
		if (sc instanceof HeapContinuation || sc.is(Nil.NIL))
			return(sc);
		int cc = (int)((Fixnum)sc).fixnum;
		// Calculate the size of the actual continuation based on the saved base pointer
		int bp = (int)((Fixnum)vm.stackIndex(cc-3)).fixnum;
		int svsize = cc - bp - 4;
		if (deepest != null && deepest[0] > bp)
			deepest[0] = bp;
		// Turn previous continuation into a heap-based one too
		ArcObject pco = vm.stackIndex(cc-1);
		pco = fromStackCont(vm, pco, deepest);
		ArcObject senv = HeapEnv.fromStackEnv(vm, vm.stackIndex(cc-2), deepest);
		HeapContinuation c = new HeapContinuation(svsize, pco, senv, (int)((Fixnum)vm.stackIndex(cc-4)).fixnum);

		for (int i=0; i<svsize; i++)
			c.setIndex(i, vm.stackIndex(bp + i));
		return(c);
	}

	@Override
	public int requiredArgs()
	{
		return(1);
	}

	/** The application of a continuation -- this will set itself as the current continuation,
	 *  ready to be restored just as the invokethread terminates. */
	@Override
	public ArcObject invoke(InvokeThread thr)
	{
		thr.thr.setCont(this);
		return(thr.getenv(0, 0));
	}
}

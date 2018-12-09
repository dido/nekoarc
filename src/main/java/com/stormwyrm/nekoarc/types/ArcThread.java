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
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.*;
import com.stormwyrm.nekoarc.util.CallSync;
import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.vm.INVALID;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import com.stormwyrm.nekoarc.vm.instruction.*;

/**
 * An Arc Thread.
 */
public class ArcThread extends ArcObject implements Callable, Runnable {
	private final static int DEFAULT_STACKSIZE = 1024;
	public final static ArcObject TYPE = Symbol.intern("thread");
	public final VirtualMachine vm;
	private int sp;					// stack pointer
	private int bp;					// base pointer
	private ArcObject env;			// environment pointer
	private ArcObject cont;			// continuation pointer
	private final ArcObject[] stack;		// stack
	private final CallSync caller;
	private int ip;					// instruction pointer
	private boolean runnable;
	private ArcObject acc;			// accumulator
	private int argc;				// argument counter for current function
	private static final INVALID NOINST = new INVALID();
	public Thread thread;			// Java thread running this
	public ArcObject here;			// "here" value used for Hanson-Lamping
	private final static Symbol noBeforesOrAfters = (Symbol) Symbol.intern("no-befores-or-afters");

	/**
	 * The instruction jump table.
	 */
	private static final Instruction[] jmptbl = {
		new NOP(),		// 0x00
		new PUSH(),		// 0x01
		new POP(),		// 0x02
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		new RET(),		// 0x0d
		NOINST,
		NOINST,
		NOINST,
		new NO(),		// 0x11
		new TRUE(),		// 0x12
		new NIL(),		// 0x13
		new HLT(),		// 0x14
		new ADD(),		// 0x15
		new SUB(),		// 0x16
		new MUL(),		// 0x17
		new DIV(),		// 0x18
		new CONS(),		// 0x19
		new CAR(),		// 0x1a
		new CDR(),		// 0x1b
		new SCAR(),		// 0x1c
		new SCDR(),		// 0x1d
		NOINST,
		new IS(),		// 0x1f
		NOINST,
		NOINST,
		NOINST,		    // used to be DUP, 0x22
		NOINST,			// 0x23
		new CONSR(),		// 0x24
		NOINST,
		new DCAR(),		// 0x26
		new DCDR(),		// 0x27
		NOINST,		    // used to be SPL, 0x28
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		new LDL(),		// 0x43
		new LDI(),		// 0x44
		new LDG(),		// 0x45
		new STG(),		// 0x46
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		new APPLY(),	// 0x4c
		new CLS(),		// 0x4d
		new JMP(),		// 0x4e
		new JT(),		// 0x4f
		new JF(),		// 0x50
		new JBND(),		// 0x51
        new CONT(),		// 0x52 -- new CONT
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		new MENV(),		// 0x65
		NOINST,
		NOINST,
		NOINST,
		new LDE0(),		// 0x69
		new STE0(),		// 0x6a
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		new LDE(),		// 0x87
		new STE(),		// 0x88
		NOINST,		    // 0x89 -- formerly CONT()
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		new ENV(),		// 0xca
		new ENVR(),		// 0xcb
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
		NOINST,
	};

	/**
	 * Create a new Arc thread
	 * @param vm The virtual machine to create this thread under
	 * @param stacksize The stack size of the thread
	 */
	public ArcThread(VirtualMachine vm, int stacksize) {
		this.vm = vm;
		sp = bp = 0;
		stack = new ArcObject[stacksize];
		ip = 0;
		runnable = true;
		env = Nil.NIL;
		cont = Nil.NIL;
		setAcc(Nil.NIL);
		caller = new CallSync();
		here = new Cons(noBeforesOrAfters, Nil.NIL);
	}

	/**
	 * Create a new Arc thread with the default stack size
	 * @param vm The virtual machine to create the thread under
	 */
	public ArcThread(VirtualMachine vm) {
		this(vm, DEFAULT_STACKSIZE);
	}

	/**
	 * Create a new Arc thread under a self-initialised virtual machine
	 * @deprecated
	 * @param stacksize Stack size for the thread
	 */
	@Deprecated
	public ArcThread(int stacksize) {
		this(new VirtualMachine(), stacksize);
	}

	@Deprecated
	public void load() {
		vm.load();
	}

	@Deprecated
	public void load(final byte[] instructions, final ArcObject[] literals) {
		vm.load(instructions, literals);
	}

	@Deprecated
	public void load(final byte[] instructions)
	{
		vm.load(instructions);
	}

	/**
	 * Halt the execution of this thread
	 */
	public void halt() {
		runnable = false;
	}

	/**
	 * Attempt to garbage collect the stack, after Richard A. Kelsey, "Tail Recursive Stack Disciplines for an
	 * Interpreter" Basically, the only things on the stack that are garbage collected are environments and
	 * continuations. The algorithm here works by copying all environments and continuations from the stack to
	 * the heap. When that's done it will move the stack pointer to the top of the stack.
	 * *
	 * 1. Start with the environment register. Move that environment and all of its children to the heap.
	 * 2. Continue with the continuation register. Copy the current continuation into the heap.
	 * 3. Compact the stack by moving the remainder of the non-stack / non-continuation elements down.
	 */
	private void stackgc() {
		int[] deepest = {sp};
		if (env instanceof Fixnum)
			env = HeapEnv.fromStackEnv(this, env, deepest);

		// If the current continuation is on the stack move it to the heap
		if (cont instanceof Fixnum)
			this.setCont(HeapContinuation.fromStackCont(this, cont, deepest));

		// If all environments and continuations have been moved to the heap, we can now
		// move all other stack elements down to the bottom.
		for (int i=0; i<sp - bp; i++)
			setStackIndex(i, stackIndex(bp + i));
		sp = (sp - bp);
		bp = 0;
	}

	/**
	 * Push an object into the thread stack
	 * @param obj the object to push
	 */
	public void push(ArcObject obj) {
		for (;;) {
			try {
				stack[sp++] = obj;
				return;
			} catch (ArrayIndexOutOfBoundsException e) {
				// Restore sp to its old value before stack gc
				sp--;
				stackgc();
				if (sp >= stack.length)
					throw new NekoArcException("stack overflow");
			}
		}
	}

	public ArcObject pop()
	{
		return(stack[--sp]);
	}

    /**
     * Four-byte instruction arguments (most everything else). Little endian.
     * @return Next four-byte instruction argument at the current IP
     */
	public int instArg() {
		long val = 0;
		int data;
		for (int i=0; i<4; i++) {
			data = (((int) vm.code()[ip++]) & 0xff);
			val |= data << i*8;
		}
		return((int)((val << 1) >> 1));
	}

	// one-byte instruction arguments (LDE/STE/ENV, etc.)
	public byte smallInstArg()
	{
		return(vm.code()[ip++]);
	}

	public ArcObject getAcc()
	{
		return acc;
	}

	public void setAcc(ArcObject acc)
	{
		this.acc = acc;
	}

	public boolean runnable()
	{
		return(this.runnable);
	}

	public ArcObject literal(int offset)
	{
		return vm.literal(offset);
	}

	public int getIP()
	{
		return ip;
	}

	public void setIP(int ip)
	{
		this.ip = ip;
	}

	public int getSP()
	{
		return(sp);
	}

	public void setSP(int sp)
	{
		this.sp = sp;
	}

	/**
	 * Number of arguments passed to the current function
	 * @return argc
	 */
	public int argc() {
		return(argc);
	}

	public int setargc(int ac)
	{
		return(argc = ac);
	}

	/**
	 * Check the number of arguments passed to a function. Throws exception if not enough or too many have been passed.
	 * @param minarg Minimum number of arguments to the function
	 * @param maxarg Maximum number of arguments to the function, or -1 if this is a variadic function with no maximum
	 */
	public void argcheck(int minarg, int maxarg) {
		if (argc() < minarg)
			throw new NekoArcException("too few arguments, at least " + minarg +
					" required, " + argc() + " passed");
		if (maxarg >= 0 && argc() > maxarg)
			throw new NekoArcException("too many arguments, at most " + maxarg +
					" allowed, " + argc() + " passed");
	}

	/**
	 * Create an environment. If there is enough space on the stack, that environment will be there, if not,
	 * it will be created in the heap.
	 * @param prevsize Number of elements already on the stack that will become part of the new environment
	 * @param extrasize Additional elements that need to be created for the new environment
	 */
	public void mkenv(int prevsize, int extrasize) {
		// Do a check for the space on the stack. If there is not enough,
		// make the environment in the heap */
		if (sp + extrasize + 3 > stack.length) {
			mkheapenv(prevsize, extrasize);
			return;
		}
		// If there is enough space on the stack, create the environment there.
		// Add the extra environment entries
		for (int i=0; i<extrasize; i++)
			push(Unbound.UNBOUND);
		int count = prevsize + extrasize;
		int envstart = sp - count;

		/* Stack environments are basically Fixnum pointers into the stack. */
		int envptr = sp;
		push(Fixnum.get(envstart));		// envptr
		push(Fixnum.get(count));		// envptr + 1
		push(env);						// envptr + 2
		env = Fixnum.get(envptr);
		bp = sp;
	}

	/**
	 * Create a new heap environment ab initio
	 * @param prevsize The previous size of elements on the stack that are to become part of the environment
	 * @param extrasize Additional elements that are to become part of the new environment
	 */
	private void mkheapenv(int prevsize, int extrasize) {
		// First, convert what will become the parent environment to a
		// heap environment if it is not already one
		env = HeapEnv.fromStackEnv(this, env);
		int envstart = sp - prevsize;
		// Create new heap environment and copy the environment values
		// from the stack into it
		HeapEnv nenv = new HeapEnv(prevsize + extrasize, env);
		for (int i=0; i<prevsize; i++)
			nenv.setEnv(i, stackIndex(envstart + i));
		// Fill in extra environment entries with UNBOUND
		for (int i=prevsize; i<prevsize+extrasize; i++)
			nenv.setEnv(i, Unbound.UNBOUND);
		bp = sp;
		env = nenv;
	}

	/**
	 * Move the current environment to the heap if needed
	 * @return The new environment in the heap
	 */
	public ArcObject heapenv() {
		env = HeapEnv.fromStackEnv(this, env);
		return(env);
	}

	/**
	 * Get the next environment
	 * @return The next environment after
	 */
	private ArcObject nextenv() {
		if (env.is(Nil.NIL))
			return(Nil.NIL);
		if (env instanceof Fixnum) {
			int index = (int)((Fixnum)env).fixnum;
			return(stackIndex(index+2));
		}
		HeapEnv e = (HeapEnv)env;
		return(e.prevEnv());
	}

	/**
	 * Find the environment at depth by traversing the list of environments
	 * @param depth The depth of environment to find
	 * @return The environment
	 */
	private ArcObject findenv(int depth) {
		ArcObject cenv = env;

		while (depth-- > 0 && !cenv.is(Nil.NIL)) {
			if (cenv instanceof Fixnum) {
				int index = (int)((Fixnum)cenv).fixnum;
				cenv = stackIndex(index+2);
			} else {
				HeapEnv e = (HeapEnv)cenv;
				cenv = e.prevEnv();
			}
		}
		return(cenv);
	}

	/**
	 * Get the value of an environment variable
	 * @param depth The depth of the environment to look up (0 for the function's own env)
	 * @param index The index of the variable
	 * @return The value of the variable
	 */
	public ArcObject getenv(int depth, int index) {
		ArcObject cenv = findenv(depth);
		if (cenv == Nil.NIL)
			throw new NekoArcException("environment depth exceeded");
		if (cenv instanceof Fixnum) {
			int si = (int)((Fixnum)cenv).fixnum;
			int start = (int)((Fixnum)stackIndex(si)).fixnum;
			int size = (int)((Fixnum)stackIndex(si+1)).fixnum;
			if (index > size)
				throw new NekoArcException("stack environment index exceeded");
			return(stackIndex(start+index));
		}
		return(((HeapEnv)cenv).getEnv(index));
	}

	/**
	 * Set an environment variable
	 * @param depth The depth of the environment variable (0 for the function's own env)
	 * @param index The index of the environment variable
	 * @param value The value to set the environment variable to
	 * @return the value that was set
	 */
	public ArcObject setenv(int depth, int index, ArcObject value) {
		ArcObject cenv = findenv(depth);
		if (cenv == Nil.NIL)
			throw new NekoArcException("environment depth exceeded");
		if (cenv instanceof Fixnum) {
			int si = (int)((Fixnum)cenv).fixnum;
			int start = (int)((Fixnum)stackIndex(si)).fixnum;
			int size = (int)((Fixnum)stackIndex(si+1)).fixnum;
			if (index > size)
				throw new NekoArcException("stack environment index exceeded");
			return(setStackIndex(start+index, value));
		}
		return(((HeapEnv)cenv).setEnv(index,value));
	}

	public ArcObject stackIndex(int index)
	{
		return(stack[index]);
	}

	private ArcObject setStackIndex(int index, ArcObject value)
	{
		return(stack[index] = value);
	}

	/**
	 * Check for stack overflow
	 * @param required How much stack space is needed to check
	 * @param message Error message to give if necessary stack space is unavailable
	 */
	public void stackcheck(int required, final String message) {
		if (sp + required > stack.length) {
			// Try to do stack gc first. If it fails, nothing for it
			stackgc();
			if (sp + required > stack.length)
				throw new NekoArcException(message);
		}
	}

	/** Make a continuation on the stack. The new continuation is
		saved in the continuation register. */
	public void makecont(int ipoffset) {
		stackcheck(4, "stack overflow while creating continuation");
		int newip = ip + ipoffset;
		push(Fixnum.get(newip));
		push(Fixnum.get(bp));
		push(env);
		push(cont);
		cont = Fixnum.get(sp);
		bp = sp;
	}

	/**
	 * Restore continuation from this thread.
	 */
	public void restorecont()
	{
		restorecont(this);
	}

	/**
	 * Restore the current continuation
	 * @param caller the caller
	 */
	public void restorecont(Callable caller) {
		if (cont instanceof Fixnum) {
			sp = (int)((Fixnum)cont).fixnum;
			cont = pop();
			setenvreg(pop());
			bp = (int)((Fixnum)pop()).fixnum;
			setIP((int)((Fixnum)pop()).fixnum);
		} else if (cont instanceof Continuation) {
			Continuation ct = (Continuation)cont;
			ct.restore(this, caller);
		} else if (cont.is(Nil.NIL)) {
			// If we have no continuation, that was an attempt to return from
			// the topmost level and we should halt the machine.
			halt();
		} else {
			throw new NekoArcException("invalid continuation");
		}
	}

	/**
	 * Set the environment register.
	 * @param env New environment
	 */
	public void setenvreg(ArcObject env)
	{
		this.env = env; 
	}

	/**
	 * Get the current continuation
	 * @return continuation register
	 */
	public ArcObject getCont()
	{
		return cont;
	}

	/**
	 * Set the continuation register
	 * @param cont the new continuation
	 */
	public void setCont(ArcObject cont)
	{
		this.cont = cont;
	}

	/**
	 * Get the type of this object
	 * @return symbol 'thread'
	 */
	@Override
	public ArcObject type() {
		return(TYPE);
	}

	@Override
	public CallSync sync() { return(caller); }

	/**
	 * Move n elements from the top of stack, overwriting the current
	 * environment.  Points the stack pointer to just above the last
	 * element moved.  Does not do this if current environment is on
	 * the heap.  It will, in either case, set the environment register to
	 * the parent environment (possibly leaving the heap environment as
	 * garbage to be collected)
	 *
	 * This is essentially used to support tail calls and tail recursion.
	 * When this mechanism is used, the new arguments are on the stack,
	 * over the previous environment.  It will move all of the new arguments
	 * onto the old environment and adjust the stack pointer appropriately.
	 * When control is transferred to the tail called function the call
	 * to mkenv will turn the stuff on the stack into a new environment.
	*/
	public void menv(int n) {
		// do nothing if we have no env
		if (env.is(Nil.NIL))
			return;
		ArcObject parentenv = nextenv();
		// We only bother if the current environment, which is to be
		// superseded, is on the stack. We move the n values previously
		// pushed to the location of the current environment,
		// overwriting it, and we set the stack pointer to the point
		// just after.
		if (env instanceof Fixnum) {
			// Destination is the previous environment
			int si = (int)((Fixnum)env).fixnum;
			int dest = (int)((Fixnum)stackIndex(si)).fixnum;
			// Source is the values on the stack
			int src = sp - n;
			System.arraycopy(stack, src, stack, dest, n);
			// new stack pointer has to point to just after moved stuff
			setSP(dest + n);
		}
		// If the current environment was on the heap, none of this black
	    // magic needs to be done.  It is enough to just set the environment
		// register to point to the parent environment (the old environment
		// thereby becoming garbage unless part of a continuation).
		setenvreg(parentenv);
	}

	/**
	 * Main entry point of the thread.
	 */
	public void run() {
		while (runnable) {
			jmptbl[(int) vm.code()[ip++] & 0xff].invoke(this);
		}
	}

	/**
	 * Hanson-Lamping reroot
	 * @param there where to reroot
	 */
	public ArcObject reroot(InvokeThread ithr, ArcObject there) {
		ArcObject before, after;

		if (here.is(there))
			return(Nil.NIL);
		reroot(ithr, there.cdr());
		before = there.car().car();
		after = there.car().cdr();
		here.scar(new Cons(after, before));
		here.scdr(there);
		there.scar(noBeforesOrAfters);
		there.scdr(Nil.NIL);
		here = there;
		return(ithr.apply(before));
	}

	/**
	 * Wait for the thread to terminate and return the last value of the accumulator
	 * @return last value of accumulator when thread finished execution
	 */
	public ArcObject join() {
		try {
			thread.join();
			return(acc);
		} catch (InterruptedException e) {
			throw new NekoArcException("thread join interrupted");
		}
	}

	@Override
	public String toString() {
		return("#<thread>");
	}
}

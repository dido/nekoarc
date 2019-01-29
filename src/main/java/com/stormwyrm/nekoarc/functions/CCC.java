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
package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.*;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;

/**
 * Builtin function ccc - Call with Current Continuation. Accepts one parameter, which is a closure.
 */
public class CCC extends Builtin {
	private static final CCC INSTANCE = new CCC();
	private CCC() {
		super("ccc", 1);
	}

	/**
	 * Get the instance of CCC.
	 * @return the unique instance of CCC
	 */
	public static Builtin getInstance() {
		return(INSTANCE);
	}

	/**
	 * Continuation wrapper used by CCC
	 */
	class ContWrapper extends ArcObject {
		private final ArcObject TYPE = Symbol.intern("continuation");
		private final ArcObject here;
		private final ArcObject origcont;

		/**
		 * Create a new continuation wrapper
		 * @param here Value of here to reroot to.
		 * @param origcont The original continuation this is supposed to wrap.
		 */
		public ContWrapper(ArcObject here, ArcObject origcont) {
			this.here = here;
			this.origcont = origcont;
		}

		@Override
		public ArcObject type() {
			return(TYPE);
		}

		@Override
		public int requiredArgs() {
			return(1);
		}

		/**
		 * Invoking the continuation sets the wrapped continuation to the current continuation, after
		 * rerooting.
		 * @param ithr The invocation thread.
		 * @return the parameter passed to the continuation when it was invoked
		 */
		@Override
		public ArcObject invoke(InvokeThread ithr) throws Throwable {
			// Hanson-Lamping reroot first
			ithr.thr.reroot(ithr, here);
			// Set original continuation as current continuation
			ithr.thr.setCont(origcont);
			return(ithr.getenv(0));
		}
	}

	/**
	 * Invoke ccc.
	 * @param ithr The thread executing ccc
	 * @return The return value of the closure passed to it.
	 */
	@Override
	public ArcObject invoke(InvokeThread ithr) throws Throwable {
		ArcObject continuation = ithr.thr.getCont();
		if (continuation instanceof Fixnum)
			continuation = HeapContinuation.fromStackCont(ithr.thr, continuation);
		return(ithr.apply(ithr.getenv(0,  0), new ContWrapper(ithr.thr.here, continuation)));
	}

}

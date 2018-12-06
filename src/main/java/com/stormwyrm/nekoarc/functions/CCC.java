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
package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.*;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;

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
	 * Invoke ccc.
	 * @param ithr The thread executing ccc
	 * @return The return value of the closure passed to it.
	 */
	@Override
	public ArcObject invoke(InvokeThread ithr) {
		ArcObject continuation = ithr.thr.getCont();
		if (continuation instanceof Fixnum)
			continuation = HeapContinuation.fromStackCont(ithr.thr, continuation);
		if (Nil.NIL.is(continuation))
			continuation = new StopContinuation(ithr.thr.here);
		if (!(continuation instanceof HeapContinuation))
			throw new NekoArcException("Invalid continuation type " + continuation.type().toString());
		return(ithr.apply(ithr.getenv(0,  0), continuation));
	}

}

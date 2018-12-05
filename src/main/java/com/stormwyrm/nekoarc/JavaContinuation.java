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
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.types.ArcThread;

public class JavaContinuation extends ArcObject implements Continuation
{
	public static final ArcObject TYPE = Symbol.intern("continuation");

	private final ArcObject prev;
	private final Callable caller;
	private final ArcObject env;

	public JavaContinuation(ArcThread vm, Callable c)
	{
		prev = vm.getCont();
		caller = c;
		env = vm.heapenv();		// copy current env to heap
	}

	@Override
	public void restore(ArcThread thr, Callable cc) {
		thr.setCont(prev);
		thr.setenvreg(env);
		// This will re-enable the thread represented by this continuation
		// This will stop the thread which restored this continuation
		if (thr == cc) {
			caller.sync().ret(thr.getAcc());
			thr.setAcc(cc.sync().retval());
		}
	}

	@Override
	public ArcObject type()
	{
		return(TYPE);
	}
}

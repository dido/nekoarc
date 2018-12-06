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
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.util.Callable;

/**
 * Continuation that stops the thread restoring it.
 */
public class StopContinuation extends HeapContinuation {
    public static final ArcObject TYPE = Symbol.intern("continuation");

    @Override
    public ArcObject type() {
        return(TYPE);
    }

    public StopContinuation(ArcObject here) {
        super(0, Nil.NIL, Nil.NIL, 0, here);
    }

    /**
     * Restore the continuation. It will simply stop the running thread.
     * @param thr virtual machine thread
     * @param caller the caller
     */
    @Override
    public void restore(ArcThread thr, Callable caller) {
        thr.halt();
    }
}

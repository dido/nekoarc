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

import com.stormwyrm.nekoarc.HeapContinuation;
import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;

public class OnErr extends Builtin {
    private static final OnErr INSTANCE = new OnErr();

    private OnErr() {
        super("on-err", 2);
    }

    /**
     * Get the instance of on-err.
     * @return the unique instance of on-err
     */
    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) throws Throwable {
        ArcObject handler = ithr.getenv(0);
        ArcObject thunk = ithr.getenv(1);
        ArcObject continuation = ithr.thr.getCont();
        if (continuation instanceof Fixnum)
            continuation = HeapContinuation.fromStackCont(ithr.thr, continuation);
        ithr.thr.onErr(handler, continuation);
        return(ithr.apply(thunk));
    }
}

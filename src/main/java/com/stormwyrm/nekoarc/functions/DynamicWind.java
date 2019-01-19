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

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;

public class DynamicWind extends Builtin {
    private static final DynamicWind INSTANCE = new DynamicWind();

    private DynamicWind() {
        super("dynamic-wind", 3);
    }

    /**
     * Get the instance of dynamic-wind.
     * @return the unique instance of dynamic-wind
     */
    public static Builtin getInstance() {
        return(INSTANCE);
    }

    /**
     * Dynamic wind.
     * @param ithr The invoke thread
     * @return the result of the 'during' thunk (parameter 1)
     */
    @Override
    public ArcObject invoke(InvokeThread ithr) throws Throwable {
        ArcObject myHere = ithr.thr.here;
        ArcObject before = ithr.getenv(0), during = ithr.getenv(1), after = ithr.getenv(2);

        ithr.thr.reroot(ithr, new Cons(new Cons(before, after), myHere));
        ArcObject retval = ithr.apply(during);
        ithr.thr.reroot(ithr, myHere);
        return(retval);
    }
}

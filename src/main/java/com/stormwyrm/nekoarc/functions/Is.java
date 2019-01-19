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
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Is extends Builtin {
    private static final Is INSTANCE = new Is();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private Is() {
        super("is", 0, 0, 0, true);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        if (ithr.argc() < 2)
            return(True.T);
        ArcObject args = ithr.getenv(0, 0);
        ArcObject x = args.car();
        args = args.cdr();
        while (!Nil.NIL.is(args)) {
            if (!x.is(args.car()))
                return(Nil.NIL);
            args = args.cdr();
        }
        return(True.T);
    }
}

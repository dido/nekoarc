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

package com.stormwyrm.nekoarc.functions.typehandling;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Coerce extends Builtin {
    private static final Coerce INSTANCE = new Coerce();

    private Coerce() {
        super("coerce", 2, 0, 0, true);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject val = ithr.getenv(0);
        ArcObject newtype = ithr.getenv(1);
        ArcObject extra = ithr.getenv(3);
        return(val.coerce(newtype, extra));
    }
}

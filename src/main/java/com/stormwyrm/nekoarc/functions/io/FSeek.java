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

package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.IOPort;

public class FSeek extends Builtin {
    private static final FSeek INSTANCE = new FSeek();

    private FSeek() {
        super("seek", 2, 1, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject sfp = ithr.getenv(0);
        ArcObject soffset = ithr.getenv(1);
        if (!(sfp instanceof IOPort))  {
            throw new NekoArcException(getName() + " expected ioport, got " + sfp + " (" + sfp.type() + ")");
        }
        IOPort fp = (IOPort)sfp;
        if (!(soffset instanceof Fixnum)) {
            throw new NekoArcException(getName() + " expected fixnum, got " + soffset + " (" + soffset.type() + ")");
        }
        long offset = ((Fixnum)soffset).fixnum;
        return(Fixnum.get(fp.seek(offset)));
    }
}

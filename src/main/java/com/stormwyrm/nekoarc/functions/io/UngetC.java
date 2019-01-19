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
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.InputPort;
import com.stormwyrm.nekoarc.types.Rune;
import com.stormwyrm.nekoarc.types.Symbol;

public class UngetC extends Builtin {
    private static final UngetC INSTANCE = new UngetC();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private UngetC() {
        super("ungetc", 1, 0, 0, true);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        Rune r = (Rune) ithr.getenv(0, 0);
        ArcObject f = ithr.getenv(0, 1).car();
        InputPort fp = (f instanceof InputPort) ? ((InputPort)f) : ((InputPort) ithr.thr.vm.value((Symbol)Symbol.intern("stdin")));
        return(fp.ungetc(r));
    }
}

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
package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.Instruction;

/**
 * LDG - Load Global. Load the value of a global binding and push to the stack
 */
public class LDGP implements Instruction {

    /**
     * LDGP - Load Global and Push. Load the value of a global binding referred to by the argument and push to stack
     * @param thr the thread executing LDGP
     * @throws NekoArcException on errors
     */
    @Override
    public void invoke(ArcThread thr) throws NekoArcException {
        int offset = thr.instArg();
        Symbol sym = (Symbol)thr.literal(offset);
        thr.push(thr.setAcc(thr.vm.value(sym)));
    }
}

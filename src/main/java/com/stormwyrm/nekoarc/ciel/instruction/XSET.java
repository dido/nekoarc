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

package com.stormwyrm.nekoarc.ciel.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.ciel.Ciel;
import com.stormwyrm.nekoarc.ciel.CielInstruction;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Vector;

public class XSET implements CielInstruction {
    @Override
    public void invoke(Ciel c) {
        long index = c.readLong();
        ArcObject obj = c.pop(), val = c.tos();

        if (val instanceof Cons && !Nil.NIL.is(val)) {
            switch ((int) index) {
                case 0:
                    val.scar(obj);
                    break;
                case 1:
                    val.scdr(obj);
                    break;
                default:
                    throw new NekoArcException("xset invalid index into cons");
            }
            return;
        } else if (val instanceof Vector) {
            Vector vec = (Vector)val;

            if (index > Integer.MAX_VALUE || index < 0)
                throw new NekoArcException("xset invalid index into vector");

            vec.setIndex((int) index, obj);
            return;
        }
        throw new NekoArcException("xset invalid type (not cons or vector)");
    }
}

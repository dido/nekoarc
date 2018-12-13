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
import com.stormwyrm.nekoarc.types.Numeric;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

/**
 * MUL - Multiply
 */
public class MUL implements Instruction {
	/**
	 * MUL - Multiply. Multiplies the top of stack to the accumulator
	 * @param thr The thread executing the multiply instruction
	 * @throws NekoArcException on error (e.g. operands that can't be multiplied)
	 */
	@Override
	public void invoke(ArcThread thr) throws NekoArcException {
		Numeric arg1, arg2;
		arg1 = (Numeric) thr.pop();
		arg2 = (Numeric) thr.getAcc();
		thr.setAcc(arg1.mul(arg2));
	}
}

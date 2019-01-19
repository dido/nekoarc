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
package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

/**
 * The APPLY instruction. Applies the accumulator, given the number of parameters in its argument
 * on the stack.
 */
public class APPLY implements Instruction {
	/**
	 * Apply the object in the accumulator to arg
	 * @param thr the thread executing the APPLY
	 * @throws NekoArcException if there is an error during the apply
	 */
	@Override
	public void invoke(ArcThread thr) throws Throwable {
		thr.setargc(thr.smallInstArg());
		thr.getAcc().apply(thr, thr);
	}

}

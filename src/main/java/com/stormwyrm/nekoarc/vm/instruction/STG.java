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
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

/**
 * STG (Store Global) instruction
 */
public class STG implements Instruction {

	/**
	 * STG - Store Global. Bind the value at top of stack to the symbol in the argument.
	 * @param thr The thread executing the STG instruction
	 * @throws NekoArcException on error
	 */
	@Override
	public void invoke(ArcThread thr) throws NekoArcException {
		int offset = thr.instArg();
		Symbol sym = (Symbol)thr.vm.literal(offset);
		thr.vm.bind(sym, thr.getAcc());
	}

}

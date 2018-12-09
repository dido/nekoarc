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
package com.stormwyrm.nekoarc.vm;

import com.stormwyrm.nekoarc.InvalidInstructionException;
import com.stormwyrm.nekoarc.types.ArcThread;

public class INVALID implements Instruction {

	/**
	 * Invalid Instruction
	 * @param thr The thread executing the instruction
	 * @throws InvalidInstructionException this is an invalid instruction!
	 */
	@Override
	public void invoke(ArcThread thr) throws InvalidInstructionException {
		throw new InvalidInstructionException(thr.getIP()-1, thr.vm.code()[thr.getIP()-1]);
	}

}

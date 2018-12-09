package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class NOP implements Instruction
{
	@Override
	public void invoke(ArcThread thr)
	{
		// No operation!

	}

}

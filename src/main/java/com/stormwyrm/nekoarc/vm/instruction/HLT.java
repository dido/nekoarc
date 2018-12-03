package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class HLT implements Instruction
{
	@Override
	public void invoke(ArcThread vm)
	{
		vm.halt();
	}

}

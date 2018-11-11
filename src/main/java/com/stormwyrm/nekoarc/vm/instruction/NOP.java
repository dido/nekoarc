package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public class NOP implements Instruction
{
	@Override
	public void invoke(VirtualMachine vm)
	{
		// No operation!

	}

}

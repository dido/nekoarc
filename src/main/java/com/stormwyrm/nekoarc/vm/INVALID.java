package com.stormwyrm.nekoarc.vm;

import com.stormwyrm.nekoarc.InvalidInstructionException;

public class INVALID implements Instruction {

	@Override
	public void invoke(VirtualMachine vm) throws InvalidInstructionException
	{
		throw new InvalidInstructionException(vm.getIP());
	}

}

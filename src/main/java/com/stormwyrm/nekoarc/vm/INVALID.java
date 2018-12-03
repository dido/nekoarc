package com.stormwyrm.nekoarc.vm;

import com.stormwyrm.nekoarc.InvalidInstructionException;
import com.stormwyrm.nekoarc.types.ArcThread;

public class INVALID implements Instruction {

	@Override
	public void invoke(ArcThread vm) throws InvalidInstructionException
	{
		throw new InvalidInstructionException(vm.getIP());
	}

}

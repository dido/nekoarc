package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class PUSH implements Instruction
{
	@Override
	public void invoke(ArcThread vm) throws NekoArcException
	{
		vm.push(vm.getAcc());
	}

}

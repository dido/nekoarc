package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class JF implements Instruction
{

	@Override
	public void invoke(ArcThread vm) throws NekoArcException
	{
		int target = vm.instArg();
		if (vm.getAcc() instanceof Nil)
			vm.setIP(vm.getIP() + target);
	}

}

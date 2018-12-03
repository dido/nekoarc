package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class JBND implements Instruction
{

	@Override
	public void invoke(ArcThread vm) throws NekoArcException
	{
		int target = vm.instArg();
		if (!vm.getAcc().is(Unbound.UNBOUND))
			vm.setIP(vm.getIP() + target);
	}

}

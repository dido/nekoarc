package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class JBND implements Instruction
{

	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		int target = thr.instArg();
		if (!thr.getAcc().is(Unbound.UNBOUND))
			thr.setIP(thr.getIP() + target);
	}

}

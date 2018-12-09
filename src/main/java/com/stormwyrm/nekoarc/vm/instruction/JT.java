package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class JT implements Instruction
{
	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		int target = thr.instArg();
		if (!(thr.getAcc() instanceof Nil))
			thr.setIP(thr.getIP() + target);
	}

}

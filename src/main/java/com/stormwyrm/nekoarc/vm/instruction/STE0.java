package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class STE0 implements Instruction
{
	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		thr.setenv(0, thr.smallInstArg() & 0xff, thr.getAcc());
	}

}

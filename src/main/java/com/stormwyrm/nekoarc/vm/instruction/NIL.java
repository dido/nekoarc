package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class NIL implements Instruction
{
	@Override
	public void invoke(ArcThread thr)
	{
		thr.setAcc(Nil.NIL);
	}
}

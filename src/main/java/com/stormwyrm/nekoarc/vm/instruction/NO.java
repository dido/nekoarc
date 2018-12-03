package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class NO implements Instruction
{
	@Override
	public void invoke(ArcThread vm) throws NekoArcException
	{
		if (vm.getAcc() instanceof Nil)
			vm.setAcc(True.T);
		else
			vm.setAcc(Nil.NIL);
	}

}

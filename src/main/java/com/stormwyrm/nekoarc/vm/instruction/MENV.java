package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class MENV implements Instruction {

	@Override
	public void invoke(ArcThread vm) throws NekoArcException
	{
		vm.menv((int)(vm.smallInstArg()));
	}

}

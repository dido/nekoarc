package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class LDI implements Instruction
{
	@Override
	public void invoke(ArcThread vm)
	{
		long value = vm.instArg();
		vm.setAcc(Fixnum.get(value));
	}
}

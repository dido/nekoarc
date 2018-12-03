package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class ADD implements Instruction
{
	@Override
	public void invoke(ArcThread vm) throws NekoArcException
	{
		ArcObject arg1, arg2;
		arg1 = vm.pop();
		arg2 = vm.getAcc();
		vm.setAcc(arg1.add(arg2));
	}

}

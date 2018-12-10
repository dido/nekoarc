package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class SCDR implements Instruction
{
	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		ArcObject arg1, arg2;
		arg1 = thr.pop();
		arg2 = thr.getAcc();
		arg1.scdr(arg2);
		thr.setAcc(arg1);
	}

}

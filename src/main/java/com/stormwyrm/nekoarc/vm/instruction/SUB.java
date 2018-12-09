package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.Numeric;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class SUB implements Instruction {

	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		Numeric arg1, arg2;
		arg1 = (Numeric) thr.pop();
		arg2 = (Numeric) thr.getAcc();
		thr.setAcc(arg1.add(arg2.negate()));
	}

}

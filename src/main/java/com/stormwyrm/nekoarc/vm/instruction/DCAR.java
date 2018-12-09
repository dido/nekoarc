package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

// just like CAR, except that it works on nil and unbound, producing unbound.
public class DCAR implements Instruction
{
	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		if (thr.getAcc().is(Nil.NIL) || thr.getAcc().is(Unbound.UNBOUND))
			thr.setAcc(Unbound.UNBOUND);
		else
			thr.setAcc(thr.getAcc().car());
	}
}

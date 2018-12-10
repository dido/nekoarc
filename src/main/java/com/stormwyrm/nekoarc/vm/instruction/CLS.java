package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.Closure;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class CLS implements Instruction
{
	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		int target = thr.instArg() + thr.getIP();
		Closure clos = new Closure(thr.heapenv(), Fixnum.get(target));
		thr.setAcc(clos);
	}

}

package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.Closure;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class CLS implements Instruction
{
	@Override
	public void invoke(ArcThread vm) throws NekoArcException
	{
		int target = vm.instArg() + vm.getIP();
		Closure clos = new Closure(vm.heapenv(), Fixnum.get(target));
		vm.setAcc(clos);
	}

}

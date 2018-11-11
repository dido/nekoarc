package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public class APPLY implements Instruction
{
	@Override
	public void invoke(VirtualMachine vm) throws NekoArcException
	{
		vm.setargc(vm.smallInstArg());
		vm.getAcc().apply(vm, vm);
	}

}

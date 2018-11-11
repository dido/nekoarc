package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

// just like CAR, except that it works on nil and unbound, producing unbound.
public class DCAR implements Instruction
{
	@Override
	public void invoke(VirtualMachine vm) throws NekoArcException
	{
		if (vm.getAcc().is(Nil.NIL) || vm.getAcc().is(Unbound.UNBOUND))
			vm.setAcc(Unbound.UNBOUND);
		else
			vm.setAcc(vm.getAcc().car());
	}
}

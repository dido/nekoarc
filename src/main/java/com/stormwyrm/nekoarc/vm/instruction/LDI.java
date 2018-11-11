package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public class LDI implements Instruction
{
	@Override
	public void invoke(VirtualMachine vm)
	{
		long value = vm.instArg();
		vm.setAcc(Fixnum.get(value));
	}
}

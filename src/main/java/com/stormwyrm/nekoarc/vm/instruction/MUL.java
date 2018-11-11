package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.Numeric;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public class MUL implements Instruction
{
	@Override
	public void invoke(VirtualMachine vm) throws NekoArcException
	{
		Numeric arg1, arg2;
		arg1 = (Numeric)vm.pop();
		arg2 = (Numeric)vm.getAcc();
		vm.setAcc(arg1.mul(arg2));
	}
}

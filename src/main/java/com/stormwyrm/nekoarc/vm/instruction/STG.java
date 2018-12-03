package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class STG implements Instruction
{

	@Override
	public void invoke(ArcThread vm) throws NekoArcException
	{
		int offset = vm.instArg();
		Symbol sym = (Symbol)vm.literal(offset);
		vm.bind(sym, vm.getAcc());
	}

}

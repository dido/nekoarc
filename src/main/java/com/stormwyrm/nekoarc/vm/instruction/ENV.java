package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class ENV implements Instruction
{
	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		int minenv, dsenv, optenv;
		minenv = thr.smallInstArg() & 0xff;
		dsenv = thr.smallInstArg() & 0xff;
		optenv = thr.smallInstArg() & 0xff;
		thr.argcheck(minenv, minenv + optenv);
		thr.mkenv(thr.argc(), minenv + optenv - thr.argc() + dsenv);
	}
}

package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.types.ArcThread;

public class ENVR implements Instruction
{
	@Override
	public void invoke(ArcThread thr) throws NekoArcException
	{
		int minenv, dsenv, optenv, i;
		minenv = thr.smallInstArg() & 0xff;
		dsenv = thr.smallInstArg() & 0xff;
		optenv = thr.smallInstArg() & 0xff;
		thr.argcheck(minenv, -1);
		ArcObject rest = Nil.NIL;
		/* Swallow as many extra arguments as are available into the rest parameter,
		 * up to the minimum + optional number of arguments. */
		for (i = thr.argc(); i>(minenv + optenv); i--)
			rest = new Cons(thr.pop(), rest);
		thr.mkenv(i, minenv + optenv - i + dsenv + 1);
		/* store the rest parameter */
		thr.setenv(0, minenv +optenv + dsenv, rest);
	}

}

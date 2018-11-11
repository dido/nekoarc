package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.vm.Instruction;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public class ENVR implements Instruction
{
	@Override
	public void invoke(VirtualMachine vm) throws NekoArcException
	{
		int minenv, dsenv, optenv, i;
		minenv = vm.smallInstArg() & 0xff;
		dsenv = vm.smallInstArg() & 0xff;
		optenv = vm.smallInstArg() & 0xff;
		vm.argcheck(minenv, -1);
		ArcObject rest = Nil.NIL;
		/* Swallow as many extra arguments as are available into the rest parameter,
		 * up to the minimum + optional number of arguments. */
		for (i = vm.argc(); i>(minenv + optenv); i--)
			rest = new Cons(vm.pop(), rest);
		vm.mkenv(i, minenv + optenv - i + dsenv + 1);
		/* store the rest parameter */
		vm.setenv(0, minenv +optenv + dsenv, rest);
	}

}

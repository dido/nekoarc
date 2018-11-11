package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public interface Continuation
{
	public void restore(VirtualMachine vm, Callable caller);
}

package com.stormwyrm.nekoarc.vm;

import com.stormwyrm.nekoarc.NekoArcException;

public interface Instruction {
	public void invoke(VirtualMachine vm) throws NekoArcException;
}

package com.stormwyrm.nekoarc.vm;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.ArcThread;

public interface Instruction {
	void invoke(ArcThread thr) throws NekoArcException;
}

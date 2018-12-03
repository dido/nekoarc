package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.types.ArcThread;

public interface Continuation
{
	void restore(ArcThread vm, Callable caller);
}

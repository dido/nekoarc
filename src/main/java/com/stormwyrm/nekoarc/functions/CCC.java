package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.HeapContinuation;
import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;

public class CCC extends Builtin
{
	protected CCC()
	{
		super("ccc", 1);
	}

	@Override
	public ArcObject invoke(InvokeThread thr)
	{
		ArcObject continuation = thr.vm.getCont();
		if (continuation instanceof Fixnum)
			continuation = HeapContinuation.fromStackCont(thr.vm, continuation);
		if (!(continuation instanceof HeapContinuation))
			throw new NekoArcException("Invalid continuation type " + continuation.type().toString());
		return(thr.apply(thr.getenv(0,  0), continuation));
	}

}

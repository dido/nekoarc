package com.stormwyrm.nekoarc.functions.arith;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;

public class Add extends Builtin
{
	private static final Add INSTANCE = new Add();

	private Add()
	{
		super("+", 0, 0, 0, true);
	}

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
	public ArcObject invoke(InvokeThread vm)
	{
		if (vm.argc() == 0)
			return(Fixnum.get(0));
		ArcObject x = vm.getenv(0, 0);
		ArcObject sum = x.car();
		x = x.cdr();
		while (!x.is(Nil.NIL)) {
			sum = sum.add(x.car());
			x = x.cdr();
		}
		return(sum);
	}
}

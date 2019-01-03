package com.stormwyrm.nekoarc.functions.arith;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Numeric;

public class Mul extends Builtin {
    private static final Mul INSTANCE = new Mul();

    private Mul() {
        super("*", 0, 0, 0, true);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        if (ithr.argc() == 0)
            return(Fixnum.get(1));
        Numeric x = (Numeric) ithr.getenv(0, 0);
        Numeric prod = (Numeric) x.car();
        x = (Numeric) x.cdr();
        while (!x.is(Nil.NIL)) {
            prod = prod.mul((Numeric) x.car());
            x = (Numeric) x.cdr();
        }
        return(prod);
    }
}

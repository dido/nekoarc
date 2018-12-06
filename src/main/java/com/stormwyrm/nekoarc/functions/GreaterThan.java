package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Orderable;

public class GreaterThan extends Builtin {
    private static final GreaterThan INSTANCE = new GreaterThan();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private GreaterThan() {
        super(">", 0, 0, 0, true);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        if (ithr.argc() < 2)
            return(True.T);
        ArcObject args = ithr.getenv(0, 0);
        ArcObject x = args.car();
        args = args.cdr();
        if (!(x instanceof Orderable))
            throw new NekoArcException("invalid comparison, " + x + " does not have order");
        Orderable o = (Orderable)x;
        while (!args.is(Nil.NIL)) {
            if (o.lessThan(args.car()) || x.is(args.car()))
                return(Nil.NIL);
            args = args.cdr();
        }
        return(True.T);
    }
}

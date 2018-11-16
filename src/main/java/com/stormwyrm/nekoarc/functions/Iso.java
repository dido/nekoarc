package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Iso extends Builtin {
    private static final Iso INSTANCE = new Iso();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private Iso() {
        super("iso", 0, 0, 0, true);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        if (vm.argc() < 2)
            return(True.T);
        ArcObject args = vm.getenv(0, 0);
        ArcObject x = args.car();
        args = args.cdr();
        while (!Nil.NIL.is(args)) {
            if (!x.iso(args.car()))
                return(Nil.NIL);
            args = args.cdr();
        }
        return(True.T);
    }
}

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;

public class SRef extends Builtin {
    private static final SRef INSTANCE = new SRef();

    private SRef() {
        super("sref", 3, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject x = ithr.getenv(0, 0);
        ArcObject val = ithr.getenv(0, 1);
        ArcObject idx = ithr.getenv(0, 2);
        return(x.sref(val, idx));
    }
}

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Exact extends Builtin {
    private static final Exact INSTANCE = new Exact();

    private Exact() {
        super("exact", 1, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        return((ithr.getenv(0,0).exactP()) ? True.T : Nil.NIL);
    }
}

package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Cadr extends Builtin {
    private static final Cadr INSTANCE = new Cadr();

    private Cadr() {
        super("cadr", 1, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        return(ithr.getenv(0,0).cdr().car());
    }
}

package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Scdr extends Builtin {
    private static final Scdr INSTANCE = new Scdr();

    private Scdr() {
        super("scdr", 2, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        return(ithr.getenv(0, 0).scdr(ithr.getenv(0, 1)));
    }
}

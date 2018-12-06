package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Cdr extends Builtin {
    private static final Cdr INSTANCE = new Cdr();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private Cdr() {
        super("cdr", 1, 0, 0, false);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        return(ithr.getenv(0,0).cdr());
    }

}

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Cddr extends Builtin {
    private static final Cddr INSTANCE = new Cddr();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private Cddr() {
        super("cddr", 1, 0, 0, false);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(vm.getenv(0,0).cdr().cdr());
    }

}

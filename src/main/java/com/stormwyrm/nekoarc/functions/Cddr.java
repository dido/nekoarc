package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Cddr extends Builtin {
    public static final Cddr CDDR = new Cddr();

    private Cddr() {
        super("cddr", 1, 0, 0, false);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(vm.getenv(0,0).cdr().cdr());
    }

}

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Scdr extends Builtin {
    public static final Scdr SCDR = new Scdr();

    private Scdr() {
        super("scdr", 2, 0, 0, false);
    }


    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(vm.getenv(0, 0).scdr(vm.getenv(0, 1)));
    }
}

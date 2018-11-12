package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Scar extends Builtin {
    private static final Scar INSTANCE = new Scar();

    private Scar() {
        super("scar", 2, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(vm.getenv(0, 0).scar(vm.getenv(0, 1)));
    }
}

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Bound extends Builtin {
    private static final Bound INSTANCE = new Bound();

    private Bound() {
        super("bound", 1, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(vm.thr.boundP(vm.getenv(0, 0)));
    }
}

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;

public class FCons extends Builtin {
    public static final FCons INSTANCE = new FCons();

    private FCons() {
        super("cons", 2, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(new com.stormwyrm.nekoarc.types.Cons(vm.getenv(0,0), vm.getenv(0,1)));
    }
}

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Cons extends Builtin {
    public static final Cons CONS = new Cons();

    private Cons() {
        super("cons", 2, 0, 0, false);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(new com.stormwyrm.nekoarc.types.Cons(vm.getenv(0,0), vm.getenv(0,1)));
    }
}

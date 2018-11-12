package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;

public class Len extends Builtin {
    private static final Len INSTANCE = new Len();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private Len() {
        super("len", 1, 0, 0, false);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(Fixnum.get(vm.getenv(0,0).len()));
    }
}

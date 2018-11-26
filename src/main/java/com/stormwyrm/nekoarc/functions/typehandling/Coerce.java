package com.stormwyrm.nekoarc.functions.typehandling;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Coerce extends Builtin {
    private static final Coerce INSTANCE = new Coerce();

    private Coerce() {
        super("coerce", 2, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        ArcObject val = vm.getenv(0);
        ArcObject newtype = vm.getenv(1);
        return(val.coerce(newtype));
    }
}

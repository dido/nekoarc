package com.stormwyrm.nekoarc.functions.typehandling;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Coerce extends Builtin {
    private static final Coerce INSTANCE = new Coerce();

    private Coerce() {
        super("coerce", 2, 0, 0, true);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject val = ithr.getenv(0);
        ArcObject newtype = ithr.getenv(1);
        ArcObject extra = ithr.getenv(3);
        return(val.coerce(newtype, extra));
    }
}

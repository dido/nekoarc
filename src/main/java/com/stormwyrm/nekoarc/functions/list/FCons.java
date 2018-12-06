package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
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
    public ArcObject invoke(InvokeThread ithr) {
        return(new com.stormwyrm.nekoarc.types.Cons(ithr.getenv(0,0), ithr.getenv(0,1)));
    }
}

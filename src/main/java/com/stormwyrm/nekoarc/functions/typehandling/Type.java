package com.stormwyrm.nekoarc.functions.typehandling;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Type extends Builtin {
    private static final Type INSTANCE = new Type();

    private Type() {
        super("type", 1, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(vm.getenv(0,0).type());
    }
}
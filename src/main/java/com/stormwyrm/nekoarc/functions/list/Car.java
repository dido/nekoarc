package com.stormwyrm.nekoarc.functions.list;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Car extends Builtin {
    public static final Car INSTANCE = new Car();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private Car() {
        super("car", 1, 0, 0, false);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        return(vm.getenv(0,0).car());
    }
}

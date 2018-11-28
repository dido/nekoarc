package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.*;

public class ReadB extends Builtin {
    private static final ReadB INSTANCE = new ReadB();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private ReadB() {
        super("readb", 0, 0, 0, true);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        InputPort fp;
        fp = (InputPort)((vm.argc() < 1) ? vm.vm.value((Symbol)Symbol.intern("stdin")) :
                vm.getenv(0, 0)).car();
        return(Fixnum.get(fp.readb()));
    }
}

package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.InputPort;
import com.stormwyrm.nekoarc.types.Symbol;

public class ReadC extends Builtin {
    private static final ReadC INSTANCE = new ReadC();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private ReadC() {
        super("readc", 0, 0, 0, true);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        InputPort fp;
        fp = (InputPort)((vm.argc() < 1) ? vm.thr.value((Symbol)Symbol.intern("stdin")) :
                vm.getenv(0, 0)).car();
        return(fp.readc());
    }
}

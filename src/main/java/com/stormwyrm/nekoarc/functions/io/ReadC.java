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
    public ArcObject invoke(InvokeThread ithr) {
        InputPort fp;
        fp = (InputPort)((ithr.argc() < 1) ? ithr.thr.vm.value((Symbol)Symbol.intern("stdin")) :
                ithr.getenv(0, 0)).car();
        return(fp.readc());
    }
}

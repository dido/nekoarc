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
    public ArcObject invoke(InvokeThread ithr) {
        InputPort fp;
        fp = (InputPort)((ithr.argc() < 1) ? ithr.thr.value((Symbol)Symbol.intern("stdin")) :
                ithr.getenv(0, 0)).car();
        return(Fixnum.get(fp.readb()));
    }
}

package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.InputPort;
import com.stormwyrm.nekoarc.types.Rune;
import com.stormwyrm.nekoarc.types.Symbol;

public class UngetC extends Builtin {
    private static final UngetC INSTANCE = new UngetC();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private UngetC() {
        super("ungetc", 1, 0, 0, true);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        Rune r = (Rune) ithr.getenv(0, 0);
        ArcObject f = ithr.getenv(0, 1).car();
        InputPort fp = (f instanceof InputPort) ? ((InputPort)f) : ((InputPort) ithr.thr.value((Symbol)Symbol.intern("stdin")));
        return(fp.ungetc(r));
    }
}

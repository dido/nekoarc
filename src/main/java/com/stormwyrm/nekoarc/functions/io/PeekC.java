
package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.InputPort;
import com.stormwyrm.nekoarc.types.Symbol;

public class PeekC extends Builtin {
    private static final PeekC INSTANCE = new PeekC();

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    private PeekC() {
        super("peekc", 0, 0, 0, true);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        InputPort fp;
        fp = (InputPort)((ithr.argc() < 1) ? ithr.thr.vm.value((Symbol)Symbol.intern("stdin")) :
                ithr.getenv(0, 0)).car();
        return(fp.peekc());
    }
}

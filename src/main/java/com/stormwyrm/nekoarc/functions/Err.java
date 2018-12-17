package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;

public class Err extends Builtin {
    private static final Err INSTANCE = new Err();

    private Err() {
        super("err", 1);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject msg = ithr.getenv(0);
        String strmsg;
        if (msg instanceof AString || msg instanceof Symbol)
            strmsg = msg.toString();
        else
            strmsg = "err expects string or symbol, got " + msg + " (" + msg.type() + ")";
        throw new NekoArcException(strmsg);
    }
}

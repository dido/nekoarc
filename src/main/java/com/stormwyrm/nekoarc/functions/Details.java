package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.AException;
import com.stormwyrm.nekoarc.types.ArcObject;

public class Details extends Builtin {
    private static final Details INSTANCE = new Details();

    private Details() {
        super("details", 1);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject ex = ithr.getenv(0);
        if (ex instanceof AException)
            return(((AException) ex).details());
        throw new NekoArcException("details expects exception, got " + ex + "(" + ex.type() + ")");
    }
}

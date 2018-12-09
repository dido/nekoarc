package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.OutString;

public class Inside extends Builtin {
    private static final Inside INSTANCE = new Inside();

    private Inside() {
        super("inside", 1, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        if (!(ithr.getenv(0) instanceof OutString))
            throw new NekoArcException("inside requires an outstring, " + ithr.getenv(0).type()  + " passed");
        return(((OutString)ithr.getenv(0)).inside());
    }
}

package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.IOPort;

public class FTell extends Builtin {
    private static final FTell INSTANCE = new FTell();

    private FTell() {
        super("tell", 1, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject sfp = ithr.getenv(0);
        if (!(sfp instanceof IOPort))  {
            throw new NekoArcException(getName() + " expected ioport, got " + sfp + " (" + sfp.type() + ")");
        }
        IOPort fp = (IOPort)sfp;
        return(Fixnum.get(fp.tell()));
    }
}

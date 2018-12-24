package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.IOPort;

public class FSeek extends Builtin {
    private static final FSeek INSTANCE = new FSeek();

    private FSeek() {
        super("seek", 2, 1, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject sfp = ithr.getenv(0);
        ArcObject soffset = ithr.getenv(1);
        if (!(sfp instanceof IOPort))  {
            throw new NekoArcException(getName() + " expected ioport, got " + sfp + " (" + sfp.type() + ")");
        }
        IOPort fp = (IOPort)sfp;
        if (!(soffset instanceof Fixnum)) {
            throw new NekoArcException(getName() + " expected fixnum, got " + soffset + " (" + soffset.type() + ")");
        }
        long offset = ((Fixnum)soffset).fixnum;
        return(Fixnum.get(fp.seek(offset)));
    }
}

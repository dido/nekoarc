package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.OutFile;
import com.stormwyrm.nekoarc.types.Symbol;

public class FOutFile extends Builtin {
    private static final FOutFile INSTANCE = new FOutFile();

    private FOutFile() {
        super("outfile", 1, 1, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject filename = ithr.getenv(0);
        boolean append = false;
        if (ithr.getenv(1).is(Symbol.intern("append")))
            append = true;
        if (!(filename instanceof AString)) {
            throw new NekoArcException(this.getName() + " expected string argument, got " + filename.toString()
                    + " (" + filename.type() + ")");
        }
        return(new OutFile(filename.toString(), append));
    }
}

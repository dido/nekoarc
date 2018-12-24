package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.InFile;
import com.stormwyrm.nekoarc.types.Symbol;

public class FInFile extends Builtin {
    private static final FInFile INSTANCE = new FInFile();

    private FInFile() {
        super("infile", 1, 1, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject filename = ithr.getenv(0);
        ArcObject mode = ithr.getenv(1);
        if (Nil.NIL.is(mode))
            mode = Symbol.intern("binary");
        if (!(filename instanceof AString)) {
            throw new NekoArcException(this.getName() + " expected string argument, got " + filename.toString()
                    + " (" + filename.type() + ")");
        }
        return(new InFile(filename.toString(), mode));
    }
}

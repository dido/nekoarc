package com.stormwyrm.nekoarc.functions.io;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.InString;
import com.stormwyrm.nekoarc.types.Symbol;

public class FInString extends Builtin {
    private static final FInString INSTANCE = new FInString();

    private FInString() {
        super("instring", 1, 1, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        if (!(vm.getenv(0) instanceof AString))
            throw new NekoArcException("cannot make an instring with " + vm.getenv(0) + " (" + vm.getenv(0).type() + ")");
        String s = vm.getenv(0).toString();
        if (Unbound.UNBOUND.is(vm.getenv(1)) || !(vm.getenv(1) instanceof Symbol))
            return(new InString(s));
        return(new InString(s, vm.getenv(1).toString()));
    }
}

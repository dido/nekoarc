package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Rune;

public class NewString extends Builtin {
    private static final NewString INSTANCE = new NewString();

    private NewString() {
        super("newstring", 1, 1, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        long count = (Fixnum.cast(ithr.getenv(0, 0), this)).fixnum;
        Rune r = (Unbound.UNBOUND.is(ithr.getenv(0, 1))) ? Rune.get(0) : (Rune) ithr.getenv(0, 1);
        return(new AString(count, r));
    }
}

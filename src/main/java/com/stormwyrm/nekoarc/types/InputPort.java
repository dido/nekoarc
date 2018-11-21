package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;

public abstract class InputPort extends ArcObject {
    public static final ArcObject TYPE = Symbol.intern("input");
    private ArcObject ungetrune = Nil.NIL;

    @Override
    public ArcObject type() {
        return(TYPE);
    }

    public abstract int readb();

    public ArcObject readc() {
        if (!Nil.NIL.is(ungetrune)) {
            ArcObject ug = ungetrune;
            ungetrune = Nil.NIL;
            return(ug);
        }
        return(Nil.NIL);
    }

    public ArcObject ungetc(Rune r) {
        return(ungetrune = r);
    }

    public ArcObject peekc() {
        ArcObject r = readc();
        if (!Nil.NIL.is(r))
            ungetc((Rune)r);
        return(r);
    }
}

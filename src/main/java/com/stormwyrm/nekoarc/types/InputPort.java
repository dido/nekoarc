package com.stormwyrm.nekoarc.types;

public abstract class InputPort extends ArcObject {
    public static final ArcObject TYPE = Symbol.intern("input");

    @Override
    public ArcObject type() {
        return(TYPE);
    }

    public abstract int readb();
    public abstract ArcObject readc();
    public abstract ArcObject peekc();
}

package com.stormwyrm.nekoarc.types;

public abstract class InputPort extends ArcObject {
    public static final ArcObject TYPE = Symbol.intern("input");

    @Override
    public ArcObject type() {
        return(TYPE);
    }

    abstract int readb();
    abstract ArcObject readc();
    abstract ArcObject peekc();
}

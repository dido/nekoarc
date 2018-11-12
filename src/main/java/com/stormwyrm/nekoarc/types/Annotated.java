package com.stormwyrm.nekoarc.types;

public class Annotated extends ArcObject {
    private ArcObject atype;
    private ArcObject arep;

    public Annotated(ArcObject t, ArcObject r) {
        atype = t;
        arep = r;
    }

    @Override
    public ArcObject type() {
        return(atype);
    }

    @Override
    public ArcObject rep() {
        return(arep);
    }

    @Override
    public String toString() {
        return("#(tagged " + atype + " " + arep + ")");
    }
}

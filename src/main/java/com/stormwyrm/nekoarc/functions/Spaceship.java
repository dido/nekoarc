package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Orderable;

public class Spaceship extends Builtin {
    public static final Spaceship INSTANCE = new Spaceship();

    private Spaceship() {
        super("cons", 2, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject left = ithr.getenv(0,0);
        ArcObject right = ithr.getenv(0,1);
        if (!(left instanceof Orderable))
            throw new NekoArcException("invalid comparison, " + left + " does not have order");
        return((((Orderable) left).lessThan(right) ? Fixnum.get(-1) : ((left.is(right)) ? Fixnum.ZERO : Fixnum.ONE)));
    }
}

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;

public class DynamicWind extends Builtin {
    private static final DynamicWind INSTANCE = new DynamicWind();

    private DynamicWind() {
        super("dynamic-wind", 3);
    }

    /**
     * Get the instance of dynamic-wind.
     * @return the unique instance of dynamic-wind
     */
    public static Builtin getInstance() {
        return(INSTANCE);
    }

    /**
     * Dynamic wind.
     * @param ithr The invoke thread
     * @return the result of the 'during' thunk (parameter 1)
     */
    @Override
    public ArcObject invoke(InvokeThread ithr) {
        ArcObject myHere = ithr.thr.here;
        ArcObject before = ithr.getenv(0), during = ithr.getenv(1), after = ithr.getenv(2);

        ithr.thr.reroot(ithr, new Cons(new Cons(before, after), myHere));
        ArcObject retval = ithr.apply(during);
        ithr.thr.reroot(ithr, myHere);
        return(retval);
    }
}

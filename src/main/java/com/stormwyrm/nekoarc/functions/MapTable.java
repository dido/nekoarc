package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Table;

public class MapTable extends Builtin {
    private static final MapTable INSTANCE = new MapTable();

    private MapTable() {
        super("maptable", 2, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread ithr) throws Throwable {
        ArcObject proc = ithr.getenv(0, 0);
        Table tbl = (Table) ithr.getenv(0, 1);
        for (ArcObject k : tbl)
            ithr.apply(proc, k, tbl.get(k));
        return(this);
    }
}

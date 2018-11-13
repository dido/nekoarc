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
    public ArcObject invoke(InvokeThread vm) {
        ArcObject proc = vm.getenv(0, 0);
        Table tbl = (Table)vm.getenv(0, 1);
        for (ArcObject k : tbl)
            vm.apply(proc, k, tbl.get(k));
        return(this);
    }
}

package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.util.ObjectMap;

import java.util.Iterator;

public class Table extends ArcObject implements Iterable<ArcObject> {
    public static final ArcObject TYPE = Symbol.intern("vector");
    private ObjectMap<ArcObject, ArcObject> table;

    public Table() {
        table = new ObjectMap<>();
    }

    public ArcObject get(ArcObject key) {
        return(table.get(key));
    }

    public ArcObject put(ArcObject value, ArcObject key) {
        return(table.put(key, value));
    }

    @Override
    public ArcObject type() {
        return(TYPE);
    }

    @Override
    public long len() {
        return(table.size);
    }

    @Override
    public ArcObject sref(ArcObject value, ArcObject index) {
        return(this.put(index, value));
    }

    @Override
    public ArcObject invoke(InvokeThread thr) {
        return(this.get(thr.getenv(0, 0)));
    }

    @Override
    public String toString() {
        return(table.toString());
    }

    @Override
    public Iterator<ArcObject> iterator() {
        return(table.iterator());
    }
}

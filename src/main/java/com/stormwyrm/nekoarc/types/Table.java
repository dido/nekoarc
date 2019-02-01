/*
 * Copyright (c) 2019 Rafael R. Sevilla
 *
 * This file is part of NekoArc
 *
 * NekoArc is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.stormwyrm.nekoarc.types;
import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.ciel.CAsm;
import com.stormwyrm.nekoarc.util.ObjectMap;

import java.util.Iterator;

/**
 * Hash Tables
 */
public class Table extends Composite implements Iterable<ArcObject> {
    public static final ArcObject TYPE = Symbol.intern("table");
    private final ObjectMap<ArcObject, ArcObject> table;

    public Table() {
        table = new ObjectMap<>();
    }

    /**
     * Get the value corresponding to the key
     * @param key The key to look up
     * @return The corresponding value, or nil if no mapping exists.
     */
    public ArcObject get(ArcObject key) {
        return(table.get(key));
    }

    /**
     * Set the value corresponding to the key
     * @param value the value to assign
     * @param key The key to be assigned
     * @return The value assigned
     */
    public ArcObject put(ArcObject value, ArcObject key) {
        return(table.put(key, value));
    }

    /**
     * Check if key corresponds to some valid value
     * @param key The key to check
     * @return True if the key has a binding in the table
     */
    public boolean hasKey(ArcObject key) {
        return(table.containsKey(key));
    }

    @Override
    public ArcObject type() {
        return(TYPE);
    }

    /**
     * Get the length of the table
     * @return The number of key/value pairs in the table
     */
    @Override
    public long len() {
        return(table.size);
    }

    /**
     * Set a reference to this table.
     * @param value The new value to assign to the index
     * @param index The index
     * @return The value assigned.
     */
    @Override
    public ArcObject sref(ArcObject value, ArcObject index) {
        return(this.put(index, value));
    }

    /**
     * Apply a table in a functional position. Basically takes first parameter as the key.
     * @param ithr The invocation thread.
     * @return The corresponding value in the first parameter
     */
    @Override
    public ArcObject invoke(InvokeThread ithr) {
        return(this.get(ithr.getenv(0, 0)));
    }

    /**
     * Get the string representation with a seen hash. Uses sharpsign-equalsign notation for
     * recursive structures.
     * @param seen the seen hash
     * @return The string representation of the table
     */
    @Override
    public String toString(ObjectMap<ArcObject, ArcObject> seen) {
        StringBuilder sb = new StringBuilder();
        if (checkReferences(seen, sb))
            return(sb.toString());
        sb.append("#hash(");
        Iterator<ArcObject> iter = this.iterator();
        while (iter.hasNext()) {
            ArcObject key = iter.next(), value = this.get(key);
            sb.append("(");
            sb.append(key.toString(seen));
            sb.append(" . ");
            sb.append(value.toString(seen));
            sb.append(")");
            if (iter.hasNext())
                sb.append(" ");
        }
        sb.append(")");
        return(sb.toString());
    }

    /**
     * Visit this hash, and then its keys and values
     * @param seen The seen hash
     * @param counter The counter for visited objects
     */
    @Override
    public void visit(ObjectMap<ArcObject, ArcObject> seen, int[] counter) {
        if (visitThis(seen, counter))
            return;
        for (ArcObject key : this) {
            key.visit(seen, counter);
            this.get(key).visit(seen, counter);
        }
    }

    @Override
    public Iterator<ArcObject> iterator() {
        return(table.iterator());
    }

    @Override
    public boolean iso(ArcObject other, ObjectMap<ArcObject,ArcObject> seen) {
        try {
            return(super.iso(other, seen));
        } catch (OOB ex) {
            // We got an Out of Band exception, so we must continue testing for iso
        }
        Table t = (Table) other;
        for (ArcObject key : this) {
            if (!t.hasKey(key))
                return(false);
            if (!this.get(key).iso(t.get(key)))
                return(false);
        }
        return(true);
    }

    @Override
    public ArcObject coerce(ArcObject newtype, ArcObject extra) {
        if (newtype == Symbol.intern("string"))
            return(new AString(this.toString()));
        return super.coerce(newtype, extra);
    }

    @Override
    public void marshal(OutputPort p, ObjectMap<ArcObject, ArcObject> seen) {
        switch (checkReferences(seen, p, CAsm.GTAB::emit)) {
            case 1:
                // do nothing if this marshals to an mget, we're done
                break;
            case 0:
                // Create table
                CAsm.GTAB.emit(p);
            case 2:
                // Fall through. Either way top of stack should contain the blank table.
                // Marshal each key/value pair and then XTSET.
                for (ArcObject key : this) {
                    this.get(key).marshal(p, seen);
                    key.marshal(p, seen);
                    CAsm.XTSET.emit(p);
                }
        }
    }
}

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

import com.stormwyrm.nekoarc.ciel.CAsm;
import com.stormwyrm.nekoarc.util.ObjectMap;

import java.util.Arrays;

/**
 * A code object
 */
public class Code extends Composite {
    public static final ArcObject TYPE = Symbol.intern("code");

    public final int ip;
    public final int size;
    public final ArcObject litlist;
    private final CodeGen cg;

    public Code(CodeGen cg, int ip, int size, ArcObject litlist) {
        this.cg = cg;
        this.ip = ip;
        this.size = size;
        this.litlist = litlist;
    }

    @Override
    public ArcObject type() {
        return(TYPE);
    }

    @Override
    public void visit(ObjectMap<ArcObject, ArcObject> seen, int[] counter) {
        if (visitThis(seen, counter))
            return;
        litlist.visit(seen, counter);
    }

    @Override
    public String toString(ObjectMap<ArcObject, ArcObject> seen) {
        return("#<code:" + this.hashCode() + "#>");
    }

    @Override
    public String toString() {
        return(toString(null));
    }

    @Override
    public void marshal(OutputPort p, ObjectMap<ArcObject, ArcObject> seen) {
        CAsm.SCODE.emit(p);
        // Write the bytecode
        CAsm.GCODE.emit(p);
        byte[] code = Arrays.copyOfRange(cg.geninst, ip, ip+size);
        CAsm.writeBinStr(p, code);
        Cons ldls = (Cons) this.litlist;
        // Write data
        for (ArcObject x : ldls) {
            long index = ((Fixnum) x).fixnum;
            cg.genlits.get(index).marshal(p, seen);
            CAsm.GDATA.emit(p);
            CAsm.writeLong(p, index);
        }
        CAsm.ECODE.emit(p);
    }
}

/*  Copyright (C) 2018 Rafael R. Sevilla

    This file is part of NekoArc

    NekoArc is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 3 of the
    License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.vm.VirtualMachine;

/**
 * A code object
 */
public class Code extends ArcObject {
    public static final ArcObject TYPE = Symbol.intern("code");

    public final int ip;
    public final int size;
    public final ArcObject ldls;

    public Code(int ip, int size, ArcObject ldls) {
        this.ip = ip;
        this.size = size;
        this.ldls = ldls;
    }

    @Override
    public ArcObject type() {
        return(TYPE);
    }
}
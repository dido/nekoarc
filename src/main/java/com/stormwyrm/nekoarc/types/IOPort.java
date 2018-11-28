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

/**
 * IOPort: Abstract base class for all I/O ports.
 */
public abstract class IOPort extends ArcObject {
    private final String name;

    /**
     * Create an input port with the given name
     * @param name the name (path) of the file
     */
    IOPort(String name) {
        this.name = name;
    }

    /**
     * Get the name of the file
     * @return the name of the file
     */
    String getName() {
        return(name);
    }

    /**
     * Is the I/O port closed?
     */
    private boolean closed = false;

    /**
     * Get closed status of the port.
     * @return True if closed.
     */
    boolean closedp() {
        return(closed);
    }

    /**
     * Close an I/O port.
     */
    public void close() {
        closed = true;
    }
}

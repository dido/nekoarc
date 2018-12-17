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

import com.stormwyrm.nekoarc.NekoArcException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * InFile - File based input port
 */
public class InFile extends InputPort {
    private final FileInputStream fp;
    /**
     * Create an input port with the given name
     *
     * @param path the path of the file
     */
    public InFile(String path, ArcObject mode) {
        super(path);
        try {
            fp = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new NekoArcException(e.getMessage());
        }
    }

    @Override
    public int readb() {
        // for closed checks
        super.readb();
        try {
            return(fp.read());
        } catch (IOException e) {
            throw new NekoArcException(e.getMessage());
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            fp.close();
        } catch (IOException e) {
            throw new NekoArcException(e.getMessage());
        }
    }
}

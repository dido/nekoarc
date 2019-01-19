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

import com.stormwyrm.nekoarc.NekoArcException;

import java.io.IOException;
import java.io.RandomAccessFile;

public class OutFile extends OutputPort {
    private final RandomAccessFile fp;
    /**
     * Create a File output port from the path
     *
     * @param name the path to the file
     * @param append true if one is to append to the file
     */
    public OutFile(String name, boolean append) {
        super(name);
        try {
            fp = new RandomAccessFile(name, "rw");
            if (append)
                fp.seek(fp.length());
        } catch (IOException e) {
            throw new NekoArcException(e.getMessage());
        }
    }

    @Override
    public int writeb(int b) {
        super.writeb(b);
        try {
            fp.write(b);
            return(b);
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

    @Override
    public long seek(long newpos) {
        try {
            fp.seek(newpos);
            return(newpos);
        } catch (IOException e) {
            throw new NekoArcException(e.getMessage());
        }
    }

    @Override
    public long tell() {
        try {
            return(fp.getFilePointer());
        } catch (IOException e) {
            throw new NekoArcException(e.getMessage());
        }
    }
}

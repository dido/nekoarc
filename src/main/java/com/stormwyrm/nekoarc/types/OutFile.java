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

package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.NekoArcException;

public abstract class OutputPort extends IOPort {
    public static final ArcObject TYPE = Symbol.intern("output");

    /**
     * Create an output port with the given name
     *
     * @param name the name (path) of the file
     */
    OutputPort(String name) {
        super(name);
    }

    /**
     * Type
     * @return the type (output)
     */
    @Override
    public ArcObject type() {
        return(TYPE);
    }

    /**
     * Write a byte to the output port
     * @param b the byte to write
     * @return the byte written
     */
    public int writeb(int b) {
        if (closedp())
            throw new NekoArcException("writeb: output port is closed");
        return(b);
    }

    /**
     * Write a character to the output port
     * @param r the rune to write
     * @return the rune written
     */
    public ArcObject writec(Rune r) {
        if (closedp())
            throw new NekoArcException("writec: output port is closed");
        return(r);
    }

    /**
     * Return a string version of the output port, including the name.
     * @return string version of the output port
     */
    @Override
    public String toString() {
        return("#<output-port:" + getName() + ">");
    }
}

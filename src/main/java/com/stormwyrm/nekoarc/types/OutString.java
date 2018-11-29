package com.stormwyrm.nekoarc.types;

import java.io.ByteArrayOutputStream;

public class OutString extends OutputPort {
    private ByteArrayOutputStream outstr;

    /**
     * Create an output port with the given name
     *
     * @param name the name (path) of the file
     */
    public OutString(String name) {
        super(name);
        outstr = new ByteArrayOutputStream();
    }

    /**
     * Write a byte to the string
     * @param b the byte to write
     * @return the byte written
     */
    @Override
    public int writeb(int b) {
        super.writeb(b);
        outstr.write(b);
        return(b);
    }

    /**
     * Get the string inside the outstring.
     * @return the string inside it
     */
    public ArcObject inside() {
        return(new AString(outstr.toString()));
    }
}

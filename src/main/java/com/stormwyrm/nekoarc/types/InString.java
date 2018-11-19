package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;

public class InString extends InputPort {
    private String str;
    private int ptr;

    public InString(String s) {
        this.str = s;
        this.ptr = 0;
    }

    @Override
    public int readb() {
        // Note this will still return the Unicode code points. Reference Arc returns UTF-8 bytes!
        if (ptr >= str.length())
            return(-1);
        return(str.codePointAt(ptr++));
    }

    @Override
    public ArcObject readc() {
        if (ptr >= str.length())
            return(Nil.NIL);
        return(Rune.get(str.codePointAt(ptr++)));

    }

    @Override
    public ArcObject peekc() {
        if (ptr >= str.length())
            return(Nil.NIL);
        return(Rune.get(str.codePointAt(ptr)));
    }
}

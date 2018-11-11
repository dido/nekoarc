package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.util.LongMap;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class Rune extends ArcObject {
    public final ArcObject TYPE = Symbol.intern("rune");
    @SuppressWarnings("WeakerAccess")
    public final int rune;
    private static final LongMap<WeakReference<Rune>> table = new LongMap<>();
    private static final ReferenceQueue<Rune> rq = new ReferenceQueue<>();

    private Rune(int x) { rune = x; }

    public static Rune get(int x)
    {
        Rune r;

        if (x < 0)
            throw new NekoArcException("invalid rune index");
        WeakReference<Rune> wrf = table.get(x);
        if (wrf != null) {
            r = wrf.get();
            if (r != null)
                return (r);
        }
        r = new Rune(x);
		table.put(x, new WeakReference<>(r, rq));
		return(r);
    }

    @Override
    public ArcObject type()
    {
        return(TYPE);
    }

    @Override
    public String toString() {
        switch (rune) {
            case 0x08:
                return("#\\backspace");
            case 0x09:
                return("#\\tab");
            case 0x0a:
                return("#\\newline");
            case 0x0b:
                return("#\\vtab");
            case 0x0c:
                return("#\\page");
            case 0x0d:
                return("#\\return");
            case 0x20:
                return("#\\space");
            case 0x7f:
                return("#\\rubout");
            default:
                if (rune < 0x0020 || (rune > 0x007f && rune < 0x0100))
                    return(String.format("#\\#u%04x", rune));
                break;
        }
        return(String.format("#\\%c", rune));
    }
}

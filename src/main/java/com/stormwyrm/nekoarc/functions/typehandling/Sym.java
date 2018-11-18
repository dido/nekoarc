package com.stormwyrm.nekoarc.functions.typehandling;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.functions.Builtin;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Rune;
import com.stormwyrm.nekoarc.types.Symbol;

public class Sym extends Builtin {
    private static final Sym INSTANCE = new Sym();

    private Sym() {
        super("sym", 1, 0, 0, false);
    }

    public static Builtin getInstance() {
        return(INSTANCE);
    }

    @Override
    public ArcObject invoke(InvokeThread vm) {
        ArcObject obj = vm.getenv(0,0);
        if (obj instanceof AString)
            return(Symbol.intern(obj.toString()));
        if (obj instanceof Rune) {
            int cp[] = new int[1];

            cp[0] = ((Rune) obj).rune;
            return (Symbol.intern(new String(cp, 0, 1)));
        }
        throw new NekoArcException("can't coerce" + obj + " to sym");
    }
}

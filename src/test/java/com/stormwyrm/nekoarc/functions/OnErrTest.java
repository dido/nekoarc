package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.Symbol;
import org.junit.Test;

import static org.junit.Assert.*;

public class OnErrTest extends TestTemplate {
    @Test
    public void testErrOnErr() {
        doTest((cg) -> {
            Op.ENV.emit(cg, 0, 0, 0);
            Op.CLS.emit(cg, "handler");
            Op.PUSH.emit(cg);
            Op.CLS.emit(cg, "thunk");
            Op.PUSH.emit(cg);
            Op.LDG.emit(cg, "on-err");
            Op.APPLY.emit(cg, 2);
            Op.RET.emit(cg);
            cg.label("handler", Op.ENV.emit(cg, 1, 0, 0));
            Op.LDLP.emit(cg, "goterr");
            Op.CONT.emit(cg, "cont1");
            Op.LDE0P.emit(cg, 0);
            Op.LDG.emit(cg, "details");
            Op.APPLY.emit(cg, 1);
            cg.label("cont1", Op.ADD.emit(cg));
            Op.RET.emit(cg);
            cg.label("thunk", Op.ENV.emit(cg, 0, 0, 0));
            Op.LDLP.emit(cg, "wcatooex");
            Op.LDG.emit(cg, "err");
            Op.APPLY.emit(cg, 1);
            Op.RET.emit(cg);
            cg.literal("on-err", Symbol.intern("on-err"));
            cg.literal("err", Symbol.intern("err"));
            cg.literal("details", Symbol.intern("details"));
            cg.literal("goterr", new AString("got error "));
            cg.literal("wcatooex", new AString("we can also throw our own exceptions"));
            return(cg);
                },
                (t) -> assertEquals("got error we can also throw our own exceptions", t.getAcc().toString()));
    }

}
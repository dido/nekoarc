package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import org.junit.Test;

import static org.junit.Assert.*;

public class OnErrTest extends TestTemplate {
    @Test
    public void testNoErrOnErr() {
        // (on-err [assign foo 100] (fn () (assign foo 200) "abc"))
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
            Op.LDI.emit(cg, 100);
            Op.STG.emit(cg, "foo");
            Op.RET.emit(cg);
            cg.label("thunk", Op.ENV.emit(cg, 0, 0, 0));
            Op.LDI.emit(cg, 200);
            Op.STG.emit(cg, "foo");
            Op.LDL.emit(cg, "abc");
            Op.RET.emit(cg);
            cg.literal("abc", new AString("abc"));
            cg.literal("on-err", Symbol.intern("on-err"));
            cg.literal("foo", Symbol.intern("foo"));
            return(cg);
        },
                (t) ->{
            assertEquals(200, ((Fixnum)(t.vm.value((Symbol) Symbol.intern("foo")))).fixnum);
            assertEquals("abc", t.getAcc().toString());
                });

    }
    @Test
    public void testErrOnErr() {
        // (on-err [+ "got error " (details _)] (fn () (err (fn () "we can also throw our own exceptions))))
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
                    return (cg);
                },
                (t) -> assertEquals("got error we can also throw our own exceptions", t.getAcc().toString()));
    }

    @Test
    public void onErrWithDynamicWind() {
        // equivalent to:
        // (on-err [assign text (+ text (details _))]
        //         (fn () (dynamic-wind (fn ())
        //                              (fn () (assign text "abc") (err "ghi"))
        //                              (fn () (assign text (+ text "def"))))))
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
                    Op.LDGP.emit(cg, "text");
                    Op.CONT.emit(cg, "C1");
                    Op.LDE0P.emit(cg, 0);
                    Op.LDG.emit(cg, "details");
                    Op.APPLY.emit(cg, 1);
                    cg.label("C1", Op.ADD.emit(cg));
                    Op.STG.emit(cg, "text");
                    Op.RET.emit(cg);
                    cg.label("thunk", Op.ENV.emit(cg, 0, 0, 0));
                    Op.CLS.emit(cg, "before");
                    Op.PUSH.emit(cg);
                    Op.CLS.emit(cg, "during");
                    Op.PUSH.emit(cg);
                    Op.CLS.emit(cg, "after");
                    Op.PUSH.emit(cg);
                    Op.LDG.emit(cg, "dynamic-wind");
                    Op.APPLY.emit(cg, 3);
                    Op.RET.emit(cg);
                    cg.label("before", Op.NIL.emit(cg));
                    Op.RET.emit(cg);
                    cg.label("during", Op.ENV.emit(cg, 0, 0, 0));
                    Op.LDL.emit(cg, "abc");
                    Op.STG.emit(cg, "text");
                    Op.LDLP.emit(cg, "ghi");
                    Op.LDG.emit(cg, "err");
                    Op.APPLY.emit(cg, 1);
                    Op.RET.emit(cg);
                    cg.label("after", Op.ENV.emit(cg, 0, 0, 0));
                    Op.LDGP.emit(cg, "text");
                    Op.LDL.emit(cg, "def");
                    Op.ADD.emit(cg);
                    Op.STG.emit(cg, "text");
                    Op.RET.emit(cg);
                    cg.literal("abc", new AString("abc"));
                    cg.literal("def", new AString("def"));
                    cg.literal("ghi", new AString("ghi"));
                    cg.literal("text", Symbol.intern("text"));
                    cg.literal("on-err", Symbol.intern("on-err"));
                    cg.literal("dynamic-wind", Symbol.intern("dynamic-wind"));
                    cg.literal("err", Symbol.intern("err"));
                    cg.literal("details", Symbol.intern("details"));
                    return (cg);
                },
                (t) -> {
                    assertEquals("abcdefghi", t.vm.value((Symbol) Symbol.intern("text")).toString());
                    assertEquals("abcdefghi", t.getAcc().toString());
                });
    }
}
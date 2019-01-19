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

package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class OnErrTest extends TestTemplate {
    @Test
    public void testNoErrOnErr() {
        // (on-err [assign foo 100] (fn () (assign foo 200) "abc"))
        doTest((cg) -> {
                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.CLS.emit(cg, "handler");
                    Op.PUSH.emit(cg);
                    Op.CLS.emit(cg, "thunk");
                    Op.PUSH.emit(cg);
                    Op.LDG.emit(cg, "on-err");
                    Op.APPLY.emit(cg, 2);
                    Op.RET.emit(cg);
                    cg.endCode();

                    cg.startCode();
                    Op.ENV.emit(cg, 1, 0, 0);
                    Op.LDI.emit(cg, 100);
                    Op.STG.emit(cg, "foo");
                    Op.RET.emit(cg);
                    Code handler = cg.endCode();

                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.LDI.emit(cg, 200);
                    Op.STG.emit(cg, "foo");
                    Op.LDL.emit(cg, "abc");
                    Op.RET.emit(cg);
                    Code thunk = cg.endCode();
                    cg.literal("abc", new AString("abc"));
                    cg.literal("on-err", Symbol.intern("on-err"));
                    cg.literal("foo", Symbol.intern("foo"));
                    cg.literal("handler", handler);
                    cg.literal("thunk", thunk);
                    return(cg);
                },
                (t) -> {
                    assertEquals(200, ((Fixnum) (t.vm.value((Symbol) Symbol.intern("foo")))).fixnum);
                    assertEquals("abc", t.getAcc().toString());
                });

    }

    @Test
    public void testErrOnErr() {
        // (on-err [+ "got error " (details _)] (fn () (err (fn () "we can also throw our own exceptions))))
        doTest((cg) -> {
                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.CLS.emit(cg, "handler");
                    Op.PUSH.emit(cg);
                    Op.CLS.emit(cg, "thunk");
                    Op.PUSH.emit(cg);
                    Op.LDG.emit(cg, "on-err");
                    Op.APPLY.emit(cg, 2);
                    Op.RET.emit(cg);
                    cg.endCode();

                    cg.startCode();
                    Op.ENV.emit(cg, 1, 0, 0);
                    Op.LDLP.emit(cg, "goterr");
                    Op.CONT.emit(cg, "cont1");
                    Op.LDE0P.emit(cg, 0);
                    Op.LDG.emit(cg, "details");
                    Op.APPLY.emit(cg, 1);
                    cg.label("cont1", Op.ADD.emit(cg));
                    Op.RET.emit(cg);
                    Code handler = cg.endCode();

                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.LDLP.emit(cg, "wcatooex");
                    Op.LDG.emit(cg, "err");
                    Op.APPLY.emit(cg, 1);
                    Op.RET.emit(cg);
                    Code thunk = cg.endCode();

                    cg.literal("on-err", Symbol.intern("on-err"));
                    cg.literal("err", Symbol.intern("err"));
                    cg.literal("details", Symbol.intern("details"));
                    cg.literal("goterr", new AString("got error "));
                    cg.literal("wcatooex", new AString("we can also throw our own exceptions"));
                    cg.literal("handler", handler);
                    cg.literal("thunk", thunk);
                    return(cg);
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
                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.CLS.emit(cg, "handler");
                    Op.PUSH.emit(cg);
                    Op.CLS.emit(cg, "thunk");
                    Op.PUSH.emit(cg);
                    Op.LDG.emit(cg, "on-err");
                    Op.APPLY.emit(cg, 2);
                    Op.RET.emit(cg);
                    cg.endCode();

                    cg.startCode();
                    Op.ENV.emit(cg, 1, 0, 0);
                    Op.LDGP.emit(cg, "text");
                    Op.CONT.emit(cg, "C1");
                    Op.LDE0P.emit(cg, 0);
                    Op.LDG.emit(cg, "details");
                    Op.APPLY.emit(cg, 1);
                    cg.label("C1", Op.ADD.emit(cg));
                    Op.STG.emit(cg, "text");
                    Op.RET.emit(cg);
                    Code handler = cg.endCode();

                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.CLS.emit(cg, "before");
                    Op.PUSH.emit(cg);
                    Op.CLS.emit(cg, "during");
                    Op.PUSH.emit(cg);
                    Op.CLS.emit(cg, "after");
                    Op.PUSH.emit(cg);
                    Op.LDG.emit(cg, "dynamic-wind");
                    Op.APPLY.emit(cg, 3);
                    Op.RET.emit(cg);
                    Code thunk = cg.endCode();

                    cg.startCode();
                    cg.label("before", Op.NIL.emit(cg));
                    Op.RET.emit(cg);
                    Code before = cg.endCode();

                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.LDL.emit(cg, "abc");
                    Op.STG.emit(cg, "text");
                    Op.LDLP.emit(cg, "ghi");
                    Op.LDG.emit(cg, "err");
                    Op.APPLY.emit(cg, 1);
                    Op.RET.emit(cg);
                    Code during = cg.endCode();

                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.LDGP.emit(cg, "text");
                    Op.LDL.emit(cg, "def");
                    Op.ADD.emit(cg);
                    Op.STG.emit(cg, "text");
                    Op.RET.emit(cg);
                    Code after = cg.endCode();

                    cg.literal("abc", new AString("abc"));
                    cg.literal("def", new AString("def"));
                    cg.literal("ghi", new AString("ghi"));
                    cg.literal("text", Symbol.intern("text"));
                    cg.literal("on-err", Symbol.intern("on-err"));
                    cg.literal("dynamic-wind", Symbol.intern("dynamic-wind"));
                    cg.literal("err", Symbol.intern("err"));
                    cg.literal("details", Symbol.intern("details"));
                    cg.literal("handler", handler);
                    cg.literal("thunk", thunk);
                    cg.literal("before", before);
                    cg.literal("during", during);
                    cg.literal("after", after);
                    return (cg);
                },
                (t) -> {
                    assertEquals("abcdefghi", t.vm.value((Symbol) Symbol.intern("text")).toString());
                    assertEquals("abcdefghi", t.getAcc().toString());
                });
    }

    @Test
    public void testDivZero() {
        doTest((cg) -> {
                    cg.startCode();
                    Op.ENV.emit(cg, 0, 0, 0);
                    Op.CLS.emit(cg, "handler");
                    Op.PUSH.emit(cg);
                    Op.CLS.emit(cg, "thunk");
                    Op.PUSH.emit(cg);
                    Op.LDG.emit(cg, "on-err");
                    Op.APPLY.emit(cg, 2);
                    Op.RET.emit(cg);
                    cg.endCode();

                    cg.startCode();
                    cg.label("handler", Op.ENV.emit(cg, 1, 0, 0));
                    Op.TRUE.emit(cg);
                    Op.RET.emit(cg);
                    Code handler = cg.endCode();

                    cg.startCode();
                    cg.label("thunk", Op.ENV.emit(cg, 0, 0, 0));
                    Op.LDIP.emit(cg, 1);
                    Op.LDI.emit(cg, 0);
                    Op.DIV.emit(cg);
                    Op.RET.emit(cg);
                    Code thunk = cg.endCode();

                    cg.literal("on-err", Symbol.intern("on-err"));
                    cg.literal("handler", handler);
                    cg.literal("thunk", thunk);
                    return(cg);
                },
                (t) -> assertTrue(True.T.is(t.getAcc())));
    }
}
package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.Closure;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

import static org.junit.Assert.*;

public class CCCTest {
    @Test
    public void testCCCsimple() {
        ArcThread thr = new ArcThread(1024);
        thr.vm.initSyms();
        // This is basically
        // (ccc (fn (esc) (esc 42) 21))

        Op.ENV.emits(thr, 0, 0, 0);
        int contpos = Op.CONT.emit(thr, 0);
        Op.LDL.emit(thr, 1);
        Op.PUSH.emit(thr);
        Op.LDG.emit(thr, 0);
        Op.APPLY.emits(thr, 1);
        int contdest = Op.RET.emit(thr);
        thr.vm.cg.patchRelativeBranch(contpos, contdest);

        // (fn (esc) (esc 42) 21)
        int func = Op.ENV.emits(thr, 1, 0, 0);
        Op.LDI.emit(thr, 42);
        Op.PUSH.emit(thr);
        Op.LDE0.emits(thr, 0);
        Op.APPLY.emits(thr, 1);
        Op.LDI.emit(thr, 21);
        Op.RET.emit(thr);

        thr.vm.cg.literal(Symbol.intern("ccc"));
        thr.vm.cg.literal(new Closure(Nil.NIL, Fixnum.get(func)));
        thr.vm.load();
        thr.setargc(0);
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(42, ((Fixnum)thr.getAcc()).fixnum);
    }

}
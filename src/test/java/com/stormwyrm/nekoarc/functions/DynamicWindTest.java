package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.*;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicWindTest {
    @Test
    public void testDWsimple() {
        // This is a very simple test that basically does
        // (dynamic-wind (fn () (assign before (+ 1 before))
        //               (fn () (assign during (+ 2 before))
        //               (fn () (assign after (+ 3 before)))
        CodeGen cg = new CodeGen();
        Op.ENV.emit(cg, 0, 0, 0);
        int contpos = Op.CONT.emit(cg, 0);
        Op.LDL.emit(cg, 1);         // before
        Op.PUSH.emit(cg);
        Op.LDL.emit(cg, 2);         // during
        Op.PUSH.emit(cg);
        Op.LDL.emit(cg, 3);         // after
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 0);         // dynamic-wind
        Op.APPLY.emit(cg, 3);
        int contdest = Op.RET.emit(cg);
        cg.patchRelativeBranch(contpos, contdest);
        int tbefore = Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 1);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 4);
        Op.ADD.emit(cg);
        Op.STG.emit(cg, 4);
        Op.RET.emit(cg);
        int tduring = Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 2);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 5);
        Op.ADD.emit(cg);
        Op.STG.emit(cg, 5);
        Op.RET.emit(cg);
        int tafter = Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 3);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 6);
        Op.ADD.emit(cg);
        Op.STG.emit(cg, 6);
        Op.RET.emit(cg);

        cg.literal(Symbol.intern("dynamic-wind"));
        cg.literal(new Closure(Nil.NIL, Fixnum.get(tbefore)));
        cg.literal(new Closure(Nil.NIL, Fixnum.get(tduring)));
        cg.literal(new Closure(Nil.NIL, Fixnum.get(tafter)));
        cg.literal(Symbol.intern("before"));
        cg.literal(Symbol.intern("during"));
        cg.literal(Symbol.intern("after"));
        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();
        vm.bind((Symbol) Symbol.intern("before"), Fixnum.get(1));
        vm.bind((Symbol) Symbol.intern("during"), Fixnum.get(2));
        vm.bind((Symbol) Symbol.intern("after"), Fixnum.get(3));

        ArcThread thr = new ArcThread(vm);
        thr.setIP(0);
        thr.run();
        ArcObject retval = thr.getAcc();
        assertEquals(4, ((Fixnum)retval).fixnum);
        assertEquals(2, ((Fixnum)vm.value((Symbol) Symbol.intern("before"))).fixnum);
        assertEquals(4, ((Fixnum)vm.value((Symbol) Symbol.intern("during"))).fixnum);
        assertEquals(6, ((Fixnum)vm.value((Symbol) Symbol.intern("after"))).fixnum);
    }

    @Test
    public void testDWCCC() {
        // dynamic-wind and call/cc interaction
        // Essentially
        // (+ (ccc (fn (cont) (dynamic-wind (fn () (assign before 1))
        //                                  (fn () (assign during 2) (cont during))
        //                                  (fn () (assign after 3))))) before during after)
        // This should of course yield 8, and before, during, and after must have the values 1, 2, and 3
        // respectively

        CodeGen cg = new CodeGen();

        int start = Op.ENV.emit(cg, 0, 0, 0);
        int contpos = Op.CONT.emit(cg, 0);
        int clspos = Op.CLS.emit(cg, 0);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 0);                     // ccc
        Op.APPLY.emit(cg, 1);
        int contdest = Op.PUSH.emit(cg);
        cg.patchRelativeBranch(contpos, contdest);
        Op.LDG.emit(cg, 2);                     // before
        Op.ADD.emit(cg);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 3);                     // during
        Op.ADD.emit(cg);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 4);                     // after
        Op.ADD.emit(cg);
        Op.RET.emit(cg);

        int clsdest = Op.ENV.emit(cg, 1, 0, 0);
        cg.patchRelativeBranch(clspos, clsdest);
        contpos = Op.CONT.emit(cg, 0);
        int sbefore = Op.CLS.emit(cg, 0);     // before
        Op.PUSH.emit(cg);
        int sduring = Op.CLS.emit(cg, 0);     // during
        Op.PUSH.emit(cg);
        int safter = Op.CLS.emit(cg, 0);       // after
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 1);                  // dynamic-wind
        Op.APPLY.emit(cg, 3);
        contdest = Op.RET.emit(cg);
        cg.patchRelativeBranch(contpos, contdest);

        int tbefore = Op.ENV.emit(cg, 0, 0, 0);
        cg.patchRelativeBranch(sbefore, tbefore);
        Op.LDI.emit(cg, 1);
        Op.STG.emit(cg, 2);                 // before
        Op.RET.emit(cg);
        int tduring = Op.ENV.emit(cg, 0, 0, 0);
        cg.patchRelativeBranch(sduring, tduring);
        Op.LDI.emit(cg, 2);
        Op.STG.emit(cg, 3);                 // during
        Op.LDG.emit(cg, 3);
        Op.PUSH.emit(cg);
        Op.LDE.emit(cg, 1, 0);         // continuation passed
        Op.APPLY.emit(cg, 1);
        Op.RET.emit(cg);
        int tafter = Op.ENV.emit(cg, 0, 0, 0);
        cg.patchRelativeBranch(safter, tafter);
        Op.LDI.emit(cg, 3);
        Op.STG.emit(cg, 4);             // after
        Op.RET.emit(cg);

        cg.literal(Symbol.intern("ccc"));
        cg.literal(Symbol.intern("dynamic-wind"));
        cg.literal(Symbol.intern("before"));
        cg.literal(Symbol.intern("during"));
        cg.literal(Symbol.intern("after"));

        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();

        ArcThread thr = new ArcThread(vm);
        thr.setIP(start);
        thr.run();
        ArcObject retval = thr.getAcc();
        assertEquals(8, ((Fixnum)retval).fixnum);
        assertEquals(1, ((Fixnum)vm.value((Symbol) Symbol.intern("before"))).fixnum);
        assertEquals(2, ((Fixnum)vm.value((Symbol) Symbol.intern("during"))).fixnum);
        assertEquals(3, ((Fixnum)vm.value((Symbol) Symbol.intern("after"))).fixnum);
    }

    @Test
    public void testDWTail() {
        // dynamic-wind in a ccc tail call, essentially
        // (ccc [+ (dynamic_wind (fn () (assign before 1))
        //                    (fn () (assign during 2) (_ during))
        //                    (fn () (assign after 3))) 1])
        CodeGen cg = new CodeGen();

        int start = Op.ENV.emit(cg, 0, 0, 0);
        int clspos = Op.CLS.emit(cg, 0);
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 0);                     // ccc
        Op.APPLY.emit(cg, 1);
        Op.RET.emit(cg);

        // [dynamic_wind ...]
        int clsdest = Op.ENV.emit(cg, 1, 0, 0);
        cg.patchRelativeBranch(clspos, clsdest);
        int contpos = Op.CONT.emit(cg, 0);
        int sbefore = Op.CLS.emit(cg, 0);     // before
        Op.PUSH.emit(cg);
        int sduring = Op.CLS.emit(cg, 0);     // during
        Op.PUSH.emit(cg);
        int safter = Op.CLS.emit(cg, 0);       // after
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 1);                  // dynamic-wind
        Op.APPLY.emit(cg, 3);
        int contdest = Op.PUSH.emit(cg);
        cg.patchRelativeBranch(contpos, contdest);
        Op.LDI.emit(cg, 1);
        Op.ADD.emit(cg);
        Op.RET.emit(cg);

        int tbefore = Op.ENV.emit(cg, 0, 0, 0);
        cg.patchRelativeBranch(sbefore, tbefore);
        Op.LDI.emit(cg, 1);
        Op.STG.emit(cg, 2);                 // before
        Op.RET.emit(cg);
        int tduring = Op.ENV.emit(cg, 0, 0, 0);
        cg.patchRelativeBranch(sduring, tduring);
        Op.LDI.emit(cg, 2);
        Op.STG.emit(cg, 3);                 // during
        Op.LDGP.emit(cg, 3);
        Op.LDE.emit(cg, 1, 0);         // continuation passed (_)
        Op.APPLY.emit(cg, 1);
        Op.RET.emit(cg);
        int tafter = Op.ENV.emit(cg, 0, 0, 0);
        cg.patchRelativeBranch(safter, tafter);
        Op.LDI.emit(cg, 3);
        Op.STG.emit(cg, 4);             // after
        Op.RET.emit(cg);

        cg.literal(Symbol.intern("ccc"));
        cg.literal(Symbol.intern("dynamic-wind"));
        cg.literal(Symbol.intern("before"));
        cg.literal(Symbol.intern("during"));
        cg.literal(Symbol.intern("after"));

        VirtualMachine vm = new VirtualMachine(cg);
        vm.initSyms();

        ArcThread thr = new ArcThread(vm);
        thr.setIP(start);
        thr.run();
        ArcObject retval = thr.getAcc();
        assertEquals(2, ((Fixnum)retval).fixnum);
        assertEquals(1, ((Fixnum)vm.value((Symbol) Symbol.intern("before"))).fixnum);
        assertEquals(2, ((Fixnum)vm.value((Symbol) Symbol.intern("during"))).fixnum);
        assertEquals(3, ((Fixnum)vm.value((Symbol) Symbol.intern("after"))).fixnum);
    }
}
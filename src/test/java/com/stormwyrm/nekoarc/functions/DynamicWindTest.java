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
        // (dynamic-wind (fn () (assign before 1)) (fn () (assign during 2)) (fn () (assign after 3)))
        CodeGen cg = new CodeGen();
        Op.ENV.emit(cg, 0, 0, 0);
        Op.LDL.emit(cg, 1);         // before
        Op.PUSH.emit(cg);
        Op.LDL.emit(cg, 2);         // during
        Op.PUSH.emit(cg);
        Op.LDL.emit(cg, 3);         // after
        Op.PUSH.emit(cg);
        Op.LDG.emit(cg, 0);         // dynamic-wind
        Op.APPLY.emit(cg, 3);
        Op.RET.emit(cg);
        int tbefore = Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 1);
        Op.STG.emit(cg, 4);
        Op.RET.emit(cg);
        int tduring = Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 2);
        Op.STG.emit(cg, 5);
        Op.RET.emit(cg);
        int tafter = Op.ENV.emit(cg, 0, 0, 0);
        Op.LDI.emit(cg, 3);
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

        ArcThread thr = new ArcThread(vm);
        thr.setIP(0);
        thr.run();
        ArcObject retval = thr.getAcc();
        assertEquals(2, ((Fixnum)retval).fixnum);
    }

}
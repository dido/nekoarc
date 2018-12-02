package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.Closure;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class CCCTest {
    @Test
    public void testCCCsimple() {
        VirtualMachine vm = new VirtualMachine(1024);
        vm.initSyms();
        // This is basically
        // (ccc (fn (esc) (esc 42) 21))

        Op.ENV.emits(vm, 0, 0, 0);
        int contpos = Op.CONT.emit(vm, 0);
        Op.LDL.emit(vm, 1);
        Op.PUSH.emit(vm);
        Op.LDG.emit(vm, 0);
        Op.APPLY.emits(vm, 1);
        int contdest = Op.RET.emit(vm);
        vm.cg.patchRelativeBranch(contpos, contdest);

        // (fn (esc) (esc 42) 21)
        int func = Op.ENV.emits(vm, 1, 0, 0);
        Op.LDI.emit(vm, 42);
        Op.PUSH.emit(vm);
        Op.LDE0.emits(vm, 0);
        Op.APPLY.emits(vm, 1);
        Op.LDI.emit(vm, 21);
        Op.RET.emit(vm);

        vm.cg.literal(Symbol.intern("ccc"));
        vm.cg.literal(new Closure(Nil.NIL, Fixnum.get(func)));
        vm.load();
        vm.setargc(0);
        assertTrue(vm.runnable());
        vm.run();
        assertFalse(vm.runnable());
        assertEquals(42, ((Fixnum)vm.getAcc()).fixnum);
    }

}
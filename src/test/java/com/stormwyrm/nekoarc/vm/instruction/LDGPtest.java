package com.stormwyrm.nekoarc.vm.instruction;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

import static org.junit.Assert.*;

public class LDGPtest {

    @Test
    public void test() {
        // ldg 0; hlt
        byte[] inst = {0x49, 0x00, 0x00, 0x00, 0x00, 0x14};

        CodeGen cg = new CodeGen();
        Op.LDGP.emit(cg, 0);
        Op.HLT.emit(cg);

        Symbol sym = (Symbol)Symbol.intern("foo");
        cg.literal(sym);
        assertEquals(cg.pos(), inst.length);
        for (int i=0; i<inst.length; i++)
            assertEquals(inst[i], cg.getAtPos(i));

        VirtualMachine vm = new VirtualMachine(cg);
        vm.load();

        ArcThread thr = new ArcThread(vm, 1024);
        thr.setAcc(Nil.NIL);
        vm.bind(sym, Fixnum.get(1234));
        assertTrue(thr.runnable());
        thr.run();
        assertFalse(thr.runnable());
        assertEquals(1234, ((Fixnum)thr.getAcc()).fixnum);
        assertEquals(1234, ((Fixnum)thr.pop()).fixnum);
        assertEquals(6, thr.getIP());
    }

}
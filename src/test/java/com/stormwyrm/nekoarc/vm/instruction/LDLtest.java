package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class LDLtest
{
	@Test
	public void test() throws NekoArcException
	{
		// ldl 0; hlt
        byte[] inst = {0x43, 0x00, 0x00, 0x00, 0x00, 0x14};
        ArcObject[] literals = new ArcObject[1];
		literals[0] = Symbol.intern("foo");
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(Symbol.intern("foo"), vm.getAcc());
		assertEquals(6, vm.getIP());		
	}
}

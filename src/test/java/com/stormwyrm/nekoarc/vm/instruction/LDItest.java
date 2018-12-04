package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class LDItest
{
	@Test
	public void testLDI() throws NekoArcException
	{
		// ldi 1; hlt
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00, 0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(Fixnum.get(1), vm.getAcc());
		assertEquals(6, vm.getIP());
		
	}
}

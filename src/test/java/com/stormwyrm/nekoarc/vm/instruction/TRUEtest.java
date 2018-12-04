package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class TRUEtest
{
	@Test
	public void testTRUE() throws NekoArcException
	{
        byte[] inst = {0x12, 0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(True.T, vm.getAcc());
		assertEquals(2, vm.getIP());
	}
}

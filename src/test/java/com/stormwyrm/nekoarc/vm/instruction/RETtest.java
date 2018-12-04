package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class RETtest
{
	@Test
	public void testNoContinuation()
	{
		byte[] inst = { 0x0d };
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(1234, ((Fixnum)vm.getAcc()).fixnum);
		assertEquals(1, vm.getIP());
	}

	@Test
	public void testContinuation()
	{
		// ret; hlt; nil; ldi 1; ret
		byte[] inst;
		inst = new byte[]{ 0x0d, 0x14, 0x13, 0x44, 0x01, 0x00, 0x00, 0x00, 0x14 };
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		vm.makecont(3);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(1, ((Fixnum)vm.getAcc()).fixnum);
	}

}

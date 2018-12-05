package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class NOtest
{
	@Test
	public void testNoNil()
	{
		// nil; no; hlt
		byte[] inst = {0x13, 0x11, 0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertTrue(vm.getAcc() instanceof True);
		assertEquals(3, vm.getIP());
	}
	
	@Test
	public void testNoTrue()
	{
		// true; no; hlt
		byte[] inst = {0x12, 0x11, 0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertTrue(vm.getAcc() instanceof Nil);
		assertEquals(3, vm.getIP());
	}

	@Test
	public void testNoNumber()
	{
		// ldi 2; no; hlt
		byte[] inst = {0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x11, 0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertTrue(vm.getAcc() instanceof Nil);
		assertEquals(7, vm.getIP());
	}

}

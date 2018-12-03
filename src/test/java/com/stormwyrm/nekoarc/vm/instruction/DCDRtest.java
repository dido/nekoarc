package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class DCDRtest
{
	@Test
	public void testBasic()
	{
		// ldi 2; push; ldi 1; cons; dcdr; hlt
		byte[] inst = {0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x19,
				0x27,
				0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(1, ((Fixnum)(vm.getAcc())).fixnum);
		assertEquals(14, vm.getIP());
	}

	@Test
	public void testNil()
	{
		// nil; dcdr; hlt
		byte[] inst = {0x13, 0x27, 0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertTrue(vm.getAcc().is(Unbound.UNBOUND));
		assertEquals(3, vm.getIP());
	}

	@Test
	public void testUnbound()
	{
		// ldl 0; dcdr; hlt
		byte[] inst = {0x43, 0x00, 0x00, 0x00, 0x00, 0x27, 0x14};
		ArcObject[] literals = new ArcObject[1];
		literals[0] = Unbound.UNBOUND;
		ArcThread vm = new ArcThread(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertTrue(vm.getAcc().is(Unbound.UNBOUND));
		assertEquals(7, vm.getIP());
	}

}

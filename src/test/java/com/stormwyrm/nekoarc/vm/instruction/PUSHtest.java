package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class PUSHtest
{
	@Test
	public void test() throws NekoArcException
	{
		// ldi 555279757; push; ldi -555729757; hlt
		byte inst[] = { 0x44, (byte) 0x5d, (byte) 0xc3, (byte) 0x1f, (byte) 0x21, 0x01,
				0x44, (byte) 0xa3, (byte) 0x3c, (byte) 0xe0, (byte) 0xde, 0x14 };
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(Fixnum.get(-555729757), vm.getAcc());
		assertEquals(1, vm.getSP());
		assertEquals(Fixnum.get(555729757), vm.pop());
		assertEquals(12, vm.getIP());
	}
}

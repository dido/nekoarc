package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class CONSRtest
{
	@Test
	public void test()
	{
		// ldi 2; push; ldi 1; consr; hlt
		byte inst[] = { 0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x24,
				0x14};
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(1, ((Fixnum)((Cons)vm.getAcc()).car()).fixnum);
		assertEquals(2, ((Fixnum)((Cons)vm.getAcc()).cdr()).fixnum);
		assertEquals(13, vm.getIP());
	}
}

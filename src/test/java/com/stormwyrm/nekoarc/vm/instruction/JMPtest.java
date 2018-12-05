package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class JMPtest
{
	@Test
	public void testJMPForward()
	{
		// ldi 1; jmp 1; nil; hlt
		byte[] inst;
        inst = new byte[]{ 0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x4e, 0x01, 0x00, 0x00, 0x00,
                0x13,
                0x14};
        ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(1, ((Fixnum)vm.getAcc()).fixnum);
		assertEquals(12, vm.getIP());
	}

	@Test
	public void testJMPBackward()
	{
		// ldi 1; jmp 2; nil; hlt; jmp -6; nil; hlt;
        byte[] inst = {0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x4e, 0x02, 0x00, 0x00, 0x00,
                0x13,
                0x14,
                0x4e, (byte) 0xfa, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                0x13,
                0x14
        };
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(1, ((Fixnum)vm.getAcc()).fixnum);
		assertEquals(12, vm.getIP());
	}
}

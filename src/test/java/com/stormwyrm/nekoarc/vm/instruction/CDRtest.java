package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class CDRtest {

	@Test
	public void test()
	{
		// ldi 2; push; ldi 1; cons; car; hlt
        byte[] inst = {0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x19,
                0x1b,
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

}

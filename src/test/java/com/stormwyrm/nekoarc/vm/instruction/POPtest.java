package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class POPtest
{
	@Test
	public void test() throws NekoArcException
	{
		// ldi 555279757; push; ldi -555729757; pop; hlt
        byte[] inst = {0x44, (byte) 0x5d, (byte) 0xc3, (byte) 0x1f, (byte) 0x21, 0x01,
                0x44, (byte) 0xa3, (byte) 0x3c, (byte) 0xe0, (byte) 0xde, 0x02, 0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.main();
		assertFalse(vm.runnable());
		assertEquals(Fixnum.get(555729757), vm.getAcc());
		assertEquals(0, vm.getSP());
		assertEquals(13, vm.getIP());
	}
}

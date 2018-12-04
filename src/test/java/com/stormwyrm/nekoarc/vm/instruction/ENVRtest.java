package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class ENVRtest
{
	/* required, optional, and rest parameters */
	@Test
	public void testROR()
	{
		// envr 1 0 2; hlt
        byte[] inst = {
                (byte) 0xcb, 0x01, 0x00, 0x02,
                0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setargc(6);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		// six parameters
		vm.push(True.T);
		vm.push(Fixnum.get(31337));
		vm.push(Fixnum.get(1337));
		vm.push(Fixnum.get(1));
		vm.push(Fixnum.get(2));
		vm.push(Fixnum.get(3));
		vm.main();
		assertFalse(vm.runnable());
		assertTrue(vm.getenv(0, 0).is(True.T));
		assertEquals(Fixnum.get(31337), vm.getenv(0, 1));
		assertEquals(Fixnum.get(1337), vm.getenv(0, 2));
		ArcObject rest = vm.getenv(0, 3);
		assertTrue(rest instanceof Cons);
		assertEquals(Fixnum.get(1), rest.car());
		assertEquals(Fixnum.get(2), rest.cdr().car());
		assertEquals(Fixnum.get(3), rest.cdr().cdr().car());
		assertEquals(5, vm.getIP());
	}

	/* required and all optional parameters only */
	@Test
	public void testRAO()
	{
		// envr 1 0 2; hlt
        byte[] inst = {
                (byte) 0xcb, 0x01, 0x00, 0x02,
                0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setargc(3);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		// three parameters
		vm.push(True.T);
		vm.push(Fixnum.get(7839));
		vm.push(Fixnum.get(646));
		vm.main();
		assertFalse(vm.runnable());
		assertTrue(vm.getenv(0, 0).is(True.T));
		assertEquals(Fixnum.get(7839), vm.getenv(0, 1));
		assertEquals(Fixnum.get(646), vm.getenv(0, 2));
		ArcObject rest = vm.getenv(0, 3);
		assertTrue(rest.is(Nil.NIL));
		assertEquals(5, vm.getIP());		
	}

	/* required and some optional parameters only */
	@Test
	public void testRSO()
	{
		// envr 1 0 2; hlt
        byte[] inst = {
                (byte) 0xcb, 0x01, 0x00, 0x02,
                0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setargc(2);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		// three parameters
		vm.push(True.T);
		vm.push(Fixnum.get(3838));
		vm.main();
		assertFalse(vm.runnable());
		assertTrue(vm.getenv(0, 0).is(True.T));
		assertEquals(Fixnum.get(3838), vm.getenv(0, 1));
		assertTrue(vm.getenv(0, 2).is(Unbound.UNBOUND));
		ArcObject rest = vm.getenv(0, 3);
		assertTrue(rest.is(Nil.NIL));
		assertEquals(5, vm.getIP());		
	}

	@Test
	public void testTooFewArgs()
	{
		// ldi 1; push; envr 3 1 1; hlt;
        byte[] inst = {0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                0x44, 0x01, 0x00, 0x00, 0x00,
                0x01,
                (byte) 0xcb, 0x03, 0x01, 0x01,
                0x14};
		ArcThread vm = new ArcThread(1024);
		vm.load(inst);
		vm.setargc(1);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		try {
			vm.main();
			fail("exception not thrown");
		} catch (NekoArcException e) {
			assertEquals("too few arguments, at least 3 required, 1 passed", e.getMessage());
		}
	}

}

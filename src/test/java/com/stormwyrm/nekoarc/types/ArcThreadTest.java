package com.stormwyrm.nekoarc.types;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Unbound;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.Fixnum;
import org.junit.Test;

public class ArcThreadTest
{	
	@Test
	public void testInstArg()
	{
		byte[] data = {0x01, 0x00, 0x00, 0x00};
		ArcThread thr = new ArcThread(1024);

		thr.load(data);
		thr.setIP(0);
		assertEquals(1, thr.instArg());

		byte[] data2 = {(byte) 0xff, 0x00, 0x00, 0x00};
		thr.load(data2);
        thr.setIP(0);
		assertEquals(255, thr.instArg());

		byte[] data3 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
		thr.load(data3);
        thr.setIP(0);
		assertEquals(-1, thr.instArg());

		byte[] data4 = {(byte) 0x5d, (byte) 0xc3, (byte) 0x1f, (byte) 0x21};
		thr.load(data4);
        thr.setIP(0);
		assertEquals(555729757, thr.instArg());
		
		// two's complement negative
		byte[] data5 = {(byte) 0xa3, (byte) 0x3c, (byte) 0xe0, (byte) 0xde};
		thr.load(data5);
        thr.setIP(0);
		assertEquals(-555729757, thr.instArg());
	}

	@Test
	public void testSmallInstArg()
	{
		byte[] data = {(byte) 0x12, (byte) 0xff};
		ArcThread thr = new ArcThread(1024);
		thr.load(data);
        thr.setIP(0);
		assertEquals(0x12, thr.smallInstArg());
		assertEquals(-1, thr.smallInstArg());
	}

	@Test
	public void testEnv() throws NekoArcException
	{
		ArcThread thr = new ArcThread(1024);

		try {
			thr.getenv(0, 0);
			fail("exception not thrown");
		} catch (NekoArcException e) {
			assertEquals("environment depth exceeded", e.getMessage());
		}

		try {
			thr.setenv(0, 0, Nil.NIL);
			fail("exception not thrown");
		} catch (NekoArcException e) {
			assertEquals("environment depth exceeded", e.getMessage());
		}

		thr.push(Fixnum.get(1));
		thr.push(Fixnum.get(2));
		thr.push(Fixnum.get(3));
		thr.mkenv(3, 2);

		thr.push(Fixnum.get(4));
		thr.push(Fixnum.get(5));
		thr.mkenv(2, 4);

		thr.setenv(1, 3, Fixnum.get(6));
		thr.setenv(0, 2, Fixnum.get(7));

		assertEquals(1, ((Fixnum)thr.getenv(1, 0)).fixnum);
		assertEquals(2, ((Fixnum)thr.getenv(1, 1)).fixnum);
		assertEquals(3, ((Fixnum)thr.getenv(1, 2)).fixnum);
		assertEquals(6, ((Fixnum)thr.getenv(1, 3)).fixnum);
		assertTrue(thr.getenv(1, 4).is(Unbound.UNBOUND));
		
		assertEquals(4, ((Fixnum)thr.getenv(0, 0)).fixnum);
		assertEquals(5, ((Fixnum)thr.getenv(0, 1)).fixnum);
		assertEquals(7, ((Fixnum)thr.getenv(0, 2)).fixnum);
		assertTrue(thr.getenv(0, 3).is(Unbound.UNBOUND));
		assertTrue(thr.getenv(0, 4).is(Unbound.UNBOUND));
		assertTrue(thr.getenv(0, 5).is(Unbound.UNBOUND));
	}

	@Test
	public void testmenv() throws NekoArcException
	{
		ArcThread thr = new ArcThread(1024);

		// New environment just as big as the old environment
		thr.push(Fixnum.get(0));
		thr.push(Fixnum.get(1));
		thr.push(Fixnum.get(2));
		assertEquals(3, thr.getSP());

		thr.mkenv(3, 0);
		assertEquals(6, thr.getSP());
		assertEquals(0, ((Fixnum)thr.getenv(0, 0)).fixnum);
		assertEquals(1, ((Fixnum)thr.getenv(0, 1)).fixnum);
		assertEquals(2, ((Fixnum)thr.getenv(0, 2)).fixnum);

		thr.push(Fixnum.get(3));
		thr.push(Fixnum.get(4));
		thr.push(Fixnum.get(5));
		thr.menv(3);
		thr.mkenv(3, 0);
		assertEquals(6, thr.getSP());
		assertEquals(3, ((Fixnum)thr.getenv(0, 0)).fixnum);
		assertEquals(4, ((Fixnum)thr.getenv(0, 1)).fixnum);
		assertEquals(5, ((Fixnum)thr.getenv(0, 2)).fixnum);

		// Reset environment and stack pointer
		thr.setenvreg(Nil.NIL);
		thr.setSP(0);

		// New environment smaller than old environment
		thr.push(Fixnum.get(0));
		thr.push(Fixnum.get(1));
		thr.push(Fixnum.get(2));
		thr.mkenv(3, 0);
		assertEquals(0, ((Fixnum)thr.getenv(0, 0)).fixnum);
		assertEquals(1, ((Fixnum)thr.getenv(0, 1)).fixnum);
		assertEquals(2, ((Fixnum)thr.getenv(0, 2)).fixnum);

		thr.push(Fixnum.get(6));
		thr.push(Fixnum.get(7));
		thr.menv(2);
		thr.mkenv(2, 0);
		assertEquals(5, thr.getSP());
		assertEquals(6, ((Fixnum)thr.getenv(0, 0)).fixnum);
		assertEquals(7, ((Fixnum)thr.getenv(0, 1)).fixnum);

		// Reset environment and stack pointer
		thr.setenvreg(Nil.NIL);
		thr.setSP(0);
	
		// New environment larger than old environment
		thr.push(Fixnum.get(0));
		thr.push(Fixnum.get(1));
		thr.push(Fixnum.get(2));
		thr.mkenv(3, 0);
		assertEquals(0, ((Fixnum)thr.getenv(0, 0)).fixnum);
		assertEquals(1, ((Fixnum)thr.getenv(0, 1)).fixnum);
		assertEquals(2, ((Fixnum)thr.getenv(0, 2)).fixnum);

		thr.push(Fixnum.get(8));
		thr.push(Fixnum.get(9));
		thr.push(Fixnum.get(10));
		thr.push(Fixnum.get(11));
		thr.menv(4);
		thr.mkenv(4, 0);
		assertEquals(7, thr.getSP());
		assertEquals(8, ((Fixnum)thr.getenv(0, 0)).fixnum);
		assertEquals(9, ((Fixnum)thr.getenv(0, 1)).fixnum);
		assertEquals(10, ((Fixnum)thr.getenv(0, 2)).fixnum);
		assertEquals(11, ((Fixnum)thr.getenv(0, 3)).fixnum);
	}
}

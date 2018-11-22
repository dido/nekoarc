package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.types.AString;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Flonum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class IStest
{

	@Test
	public void testSameFixnums()
	{
		// ldi 1234; push; ldi 1234; is; hlt
		byte inst[] = { 0x44, (byte)0xd2, 0x04, 0x00, 0x00,
				0x01,
				0x44, (byte)0xd2, 0x04, 0x00, 0x00,
				0x1f,
				0x14 };
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, 0);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(True.T, vm.getAcc());
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testDiffFixnums()
	{
		// ldi 1234; push; ldi 1235; is; hlt
		byte inst[] = { 0x44, (byte)0xd2, 0x04, 0x00, 0x00,
				0x01,
				0x44, (byte)0xd3, 0x04, 0x00, 0x00,
				0x1f,
				0x14 };
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, 0);
		vm.setAcc(True.T);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(Nil.NIL, vm.getAcc());
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testFixnumFlonum()
	{
		// ldi 1; push; ldl 0; is; hlt
		byte inst[] = { 0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1f,
				0x14};
		ArcObject literals[] = new ArcObject[1];
		literals[0] = new Flonum(1.0);
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals, 0);
		vm.setAcc(True.T);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(Nil.NIL, vm.getAcc());
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testSameFlonums()
	{
		// ldl 0; push; ldl 1; add; hlt
		byte inst[] = { 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1f,
				0x14};
		ArcObject literals[] = new ArcObject[2];
		literals[0] = new Flonum(1.0);
		literals[1] = new Flonum(1.0);
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals, 0);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(True.T, vm.getAcc());
		assertEquals(13, vm.getIP());
	}	

	@Test
	public void testDiffFlonums()
	{
		// ldl 0; push; ldl 1; add; hlt
		byte inst[] = { 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1f,
				0x14};
		ArcObject literals[] = new ArcObject[2];
		literals[0] = new Flonum(1.0);
		literals[1] = new Flonum(2.0);
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals, 0);
		vm.setAcc(True.T);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(Nil.NIL, vm.getAcc());
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testSameSymbol()
	{
		// ldl 0; push; ldl 1; is; hlt
		byte inst[] = { 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1f,
				0x14};
		ArcObject literals[] = new ArcObject[2];
		literals[0] = Symbol.intern("foo");
		literals[1] = Symbol.intern("foo");
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals, 0);
		vm.setAcc(Nil.NIL);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(True.T, vm.getAcc());
		assertEquals(13, vm.getIP());
	}	

	@Test
	public void testDiffSymbol()
	{
		// ldl 0; push; ldl 1; add; hlt
		byte inst[] = { 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1f,
				0x14};
		ArcObject literals[] = new ArcObject[2];
		literals[0] = Symbol.intern("foo");
		literals[1] = Symbol.intern("bar");
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals, 0);
		vm.setAcc(True.T);
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(Nil.NIL, vm.getAcc());
		assertEquals(13, vm.getIP());
	}	

	@Test
	public void testSameStrings()
	{
		// ldl 0; push; ldl 1; is; hlt
		byte inst[] = { 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1f,
				0x14};
		ArcObject literals[] = new ArcObject[2];
		literals[0] = new AString("foo");
		literals[1] = new AString("foo");
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals, 0);
		vm.setAcc(Fixnum.get(0));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(True.T, vm.getAcc());
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testDiffStrings()
	{
		// ldl 0; push; ldl 1; is; hlt
		byte inst[] = { 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1f,
				0x14};
		ArcObject literals[] = new ArcObject[2];
		literals[0] = new AString("foo");
		literals[1] = new AString("bar");
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals, 0);
		vm.setAcc(Fixnum.get(0));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(Nil.NIL, vm.getAcc());
		assertEquals(13, vm.getIP());
	}

	@Test
	public void testNils()
	{
		// ldl 0; push; ldl 1; is; hlt
		byte inst[] = { 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x1f,
				0x14};
		ArcObject literals[] = new ArcObject[2];
		literals[0] = Nil.NIL;
		literals[1] = Nil.EMPTY_LIST;
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals, 0);
		vm.setAcc(Fixnum.get(0));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(True.T, vm.getAcc());
		assertEquals(13, vm.getIP());
	}
}

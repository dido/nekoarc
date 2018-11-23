package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.vm.VirtualMachine;
import org.junit.Test;

public class LDGtest
{
	@Test
	public void test()
	{
		// ldg 0; hlt
		byte inst[] = { 0x45, 0x00, 0x00, 0x00, 0x00, 0x14 };
		ArcObject literals[] = new ArcObject[1];
		Symbol sym = (Symbol)Symbol.intern("foo");
		literals[0] = sym;
		VirtualMachine vm = new VirtualMachine(1024);
		vm.load(inst, literals);
		vm.setAcc(Nil.NIL);
		vm.bind(sym, Fixnum.get(1234));
		assertTrue(vm.runnable());
		vm.run();
		assertFalse(vm.runnable());
		assertEquals(1234, ((Fixnum)vm.getAcc()).fixnum);
		assertEquals(6, vm.getIP());
	}

}

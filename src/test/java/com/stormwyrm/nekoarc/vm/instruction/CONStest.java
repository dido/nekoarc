package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.types.Cons;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class CONStest extends TestTemplate {

	@Test
	public void test() {
		// ldi 2; push; ldi 1; cons; hlt
        doTestWithByteCode(new byte[]{0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x01,
                0x44, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                0x19,
                0x14},
				(cg) ->{
        	Op.LDI.emit(cg, 2);
        	Op.PUSH.emit(cg);
        	Op.LDI.emit(cg, 1);
        	Op.CONS.emit(cg);
        	Op.HLT.emit(cg);
        	return(cg);
				},
				(t)->{
        	assertTrue(t.getAcc() instanceof Cons);
			assertEquals(2, ((Fixnum)t.getAcc().car()).fixnum);
			assertEquals(1, ((Fixnum)t.getAcc().cdr()).fixnum);
				});
	}

}

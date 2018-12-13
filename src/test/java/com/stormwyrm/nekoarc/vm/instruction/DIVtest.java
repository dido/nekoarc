package com.stormwyrm.nekoarc.vm.instruction;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Fixnum;
import com.stormwyrm.nekoarc.types.Flonum;
import com.stormwyrm.nekoarc.types.ArcThread;
import org.junit.Test;

public class DIVtest extends TestTemplate {
	@Test
	public void testFixnumDivFixnum1() {
		// ldi 8; push; ldi 2; div; hlt
		doTestWithByteCode(new byte[]{0x44, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						0x01,
						0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						0x18,
						0x14},
				(cg)-> {
					Op.LDI.emit(cg, 8);
					Op.PUSH.emit(cg);
					Op.LDI.emit(cg, 2);
					Op.DIV.emit(cg);
					Op.HLT.emit(cg);
					return(cg);
				}, Fixnum.get(4));
	}

	@Test
	public void testFixnumDivFixnum2() throws NekoArcException {
		// ldi 2; push; ldi 8; div; hlt
		doTestWithByteCode(new byte[]{0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x44, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x18,
				0x14},
				(cg) -> {
					Op.LDI.emit(cg, 2);
					Op.PUSH.emit(cg);
					Op.LDI.emit(cg, 8);
					Op.DIV.emit(cg);
					Op.HLT.emit(cg);
					return(cg);
				}, Fixnum.ZERO);
	}

	@Test
	public void testFlonumDivFixnum() throws NekoArcException {
		// ldl 0; push; ldi 2; div; hlt
		doTestWithByteCode(new byte[]{0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x44, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x18,
				0x14},
				(cg) -> {
					Op.LDL.emit(cg, "pi");
					Op.PUSH.emit(cg);
					Op.LDI.emit(cg, 2);
					Op.DIV.emit(cg);
					Op.HLT.emit(cg);
					cg.literal("pi", new Flonum(3.1415926535));
					return(cg);
			}, (t)-> assertEquals(1.570796325, ((Flonum)t.getAcc()).flonum, 1e-6));
	}

	@Test
	public void testFlonumDivFlonum() throws NekoArcException {
		// ldl 0; push; ldl 1; div; hlt
		doTestWithByteCode(new byte[]{0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x01,
				0x43, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				0x18,
				0x14},
				(cg) -> {
					Op.LDL.emit(cg, "pi");
					Op.PUSH.emit(cg);
					Op.LDL.emit(cg, "e");
					Op.DIV.emit(cg);
					Op.HLT.emit(cg);
					cg.literal("pi", new Flonum(3.1415926535));
					cg.literal("e", new Flonum(2.7182818285));
					return(cg);
			}, (t)-> assertEquals(1.155727349740476, ((Flonum)t.getAcc()).flonum, 1e-6));
	}

}

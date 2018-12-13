package com.stormwyrm.nekoarc.vm;

import static org.junit.Assert.*;

import com.stormwyrm.nekoarc.MakeCode;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.Op;
import com.stormwyrm.nekoarc.TestTemplate;
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.Fixnum;
import org.junit.Test;

public class TailRecursionTest extends TestTemplate {
	/*
	 * This is a simple test for tail recursion. Essentially
	 * (fn (x y t) (+ ((afn (acc z) (if (is z 0) acc (self (+ acc x) (- z 1)))) 0 y) t))
	 * For large enough values of y the stack would overflow with garbage environments
	 * unless we do tail recursion properly.
	 */
	@Test
	public void test() {
		// Reduce thread stack size to really small so that if we don't do tail recursion optimization properly
		// we will overflow the stack.
		stackSize = 14;
		// (fn (x y t) (+ ...)
		// env 3 1 0; cont 19; ldi 0; push; lde0 1; push; cls 7; ste0 3; apply 2; push; lde0 2; add; ret;
		// (note that there are no cont instructions in the second function because all calls are tail calls)
		// (afn (acc z) (if (is z 0) acc (self (+ acc x) (- z 1)))) 0 y) t)
		// env 2 0 0; lde0 1; push; ldi 0; is; jf xxx; lde0 0; ret; lde0 0; push; lde 1 0; add; push; lde0 1;
		// push; ldi 1; sub; push; lde 1 2; apply 2; ret
		byte[] inst = {(byte) 0xca, 0x03, 0x01, 0x00,    // env 3 1 0
				(byte) 0x52, 0x12, 0x00, 0x00, 0x00,        // cont 18
				0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
				0x01,                                    // push
				0x69, 0x01,                                // lde0 1
				0x01,                                    // push
				0x4d, 0x09, 0x00, 0x00, 0x00,            // cls 9
				0x6a, 0x03,                                // ste0 3
				0x4c, 0x02,                                // apply 2
				0x01,                                    // push
				0x69, 0x02,                                // lde0 2
				0x15,                                    // add
				0x0d,                                    // ret
				(byte) 0xca, 0x02, 0x00, 0x00,            // env 2 0 0
				0x69, 0x01,                                // lde0 1
				0x01,                                    // push
				0x44, 0x00, 0x00, 0x00, 0x00,            // ldi 0
				0x1f,                                    // is
				0x50, 0x03, 0x00, 0x00, 0x00,            // jf 3
				0x69, 0x00,                                // lde0 0
				0x0d,                                    // ret
				0x69, 0x00,                                // lde0 0
				0x01,                                    // push
				(byte) 0x87, 0x01, 0x00,                    // lde 1 0
				0x15,                                    // add
				0x01,                                    // push
				0x69, 0x01,                                // lde0 1
				0x01,                                    // push
				0x44, 0x01, 0x00, 0x00, 0x00,            // ldi 1
				0x16,                                    // sub
				0x01,                                    // push
				(byte) 0x87, 0x01, 0x03,                    // lde 1 3
				0x4c, 0x02,                                // apply 2
				0x0d                                    // ret
		};

		MakeCode mc = (cg) -> {
			Op.ENV.emit(cg, 3, 1, 0);
			Op.CONT.emit(cg, "LBL0");
			Op.LDI.emit(cg, 0);
			Op.PUSH.emit(cg);
			Op.LDE0.emit(cg, 1);
			Op.PUSH.emit(cg);
			Op.CLS.emit(cg, "LAMBDA");
			Op.STE0.emit(cg, 3);
			Op.APPLY.emit(cg, 2);
			cg.label("LBL0", Op.PUSH.emit(cg));
			Op.LDE0.emit(cg, 2);
			Op.ADD.emit(cg);
			Op.RET.emit(cg);
			cg.label("LAMBDA", Op.ENV.emit(cg, 2, 0, 0));
			Op.LDE0.emit(cg, 1);
			Op.PUSH.emit(cg);
			Op.LDI.emit(cg, 0);
			Op.IS.emit(cg);
			Op.JF.emit(cg, "LBL1");
			Op.LDE0.emit(cg, 0);
			Op.RET.emit(cg);
			cg.label("LBL1",  Op.LDE0.emit(cg, 0));
			Op.PUSH.emit(cg);
			Op.LDE.emit(cg, 1, 0);
			Op.ADD.emit(cg);
			Op.PUSH.emit(cg);
			Op.LDE0.emit(cg, 1);
			Op.PUSH.emit(cg);
			Op.LDI.emit(cg, 1);
			Op.SUB.emit(cg);
			Op.PUSH.emit(cg);
			Op.LDE.emit(cg, 1, 3);
			Op.APPLY.emit(cg, 2);
			Op.RET.emit(cg);
			return(cg);
		};

		doTestWithByteCode(inst, mc,
				(t)->{
			t.setargc(3);
			t.push(Fixnum.get(1));
			t.push(Fixnum.get(50000));
			t.push(Fixnum.get(1));
			t.setAcc(Nil.NIL);
				},
				(t)-> assertEquals(50001, ((Fixnum)t.getAcc()).fixnum));
	}

}

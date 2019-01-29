/*
 * Copyright (c) 2019 Rafael R. Sevilla
 *
 * This file is part of NekoArc
 *
 * NekoArc is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.stormwyrm.nekoarc.ciel;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.InputPort;
import com.stormwyrm.nekoarc.types.Rune;
import com.stormwyrm.nekoarc.util.LongMap;

/**
 * CIEL virtual machine context
 */
public class Ciel {
    private final static int DEFAULT_STACKSIZE = 1024;
    private final ArcObject[] stack;        // stack
    private final LongMap<ArcObject> memo;  // memo
    private int sp;

    private final InputPort fp;
    private final CodeGen cg;
    private boolean runnable;

    public Ciel(InputPort fp, CodeGen cg, int stacksize) {
        stack = new ArcObject[stacksize];
        sp = 0;
        this.fp = fp;
        runnable = true;
        this.cg = cg;
        memo = new LongMap<>();
    }

    public Ciel(InputPort fp, CodeGen cg) {
        this(fp, cg, DEFAULT_STACKSIZE);
    }

    public Ciel(InputPort fp) { this(fp, new CodeGen(), DEFAULT_STACKSIZE); }

    public CodeGen getCG() {
        return(cg);
    }

    public InputPort port() { return(fp); }

    public void push(ArcObject obj) {
        try {
            stack[sp++] = obj;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NekoArcException("Stack overflow in CIEL");
        }
    }

    /**
     * Pop the stack
     * @return Topmost element
     */
    public ArcObject pop() {
        return(stack[--sp]);
    }

    /**
     * Get the current top of stack
     * @return Current top of stack
     */
    public ArcObject tos() { return(stack[sp-1]); }

    public double readDouble() {
        long raw = 0;
        int shiftcount = 0;
        for (int i=0; i<8; i++) {
            raw |= ((long)(fp.readb() & 0xff)) << shiftcount;
            shiftcount += 8;
        }
        return(Double.longBitsToDouble(raw));
    }

    public long readLong() {
        long val = 0, x;
        int shiftcount = 0;
        byte b;

        for (;;) {
            b = (byte) (fp.readb() & 0xff);
            if ((b & 0x80) != 0)
                break;
            if (shiftcount >= 63)
                throw new NekoArcException("long value exceeded");
            x = b;
            x <<= shiftcount;
            val |= x;
            shiftcount += 7;
        }
        if ((b & 0xc0) == 0x80) {
            // non-negative, clear the topmost bit only
            b ^= 0x80;
        }
        x = b;
        x <<= shiftcount;
        val |= x;
        return(val);
    }

    public String readString() {
        StringBuilder sb = new StringBuilder();
        long len = readLong();
        for (int i=0; i<len; i++) {
            ArcObject r = fp.readc();
            if (Nil.NIL.is(r))
                throw new NekoArcException("unterminated string reaches end of file in Ciel marshalled data");
            sb.appendCodePoint(((Rune)r).rune);
        }
        return(sb.toString());
    }

    public void load() {
        while (runnable) {
            int op = fp.readb();
            if (op < 0) {
                runnable = false;
                break;
            }
            CielJmpTbl.jmptbl[op].invoke(this);
        }
    }

    /**
     * Set a memo value
     * @param index Memo index
     * @param value Value to store
     * @return Value stored
     */
    public ArcObject setMemo(long index, ArcObject value) {
        memo.put(index, value);
        return(value);
    }

    /**
     * Get a memo value
     * @param index Index into the memo
     * @return Value stored at index
     */
    public ArcObject getMemo(long index) {
        return(memo.get(index));
    }
}

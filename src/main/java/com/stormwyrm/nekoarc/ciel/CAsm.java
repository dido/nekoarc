
/* This file is automatically generated -- DO NOT EDIT! */

package com.stormwyrm.nekoarc.ciel;

import com.stormwyrm.nekoarc.types.OutputPort;
import com.stormwyrm.nekoarc.types.Rune;

public enum CAsm {
    GNIL(0x0),
    GTRUE(0x8),
    GFIX(0x10),
    GFLO(0x18),
    GRUNE(0x20),
    GSTR(0x30),
    LAST(0xff);

    /**
     * The opcode for the instruction
     */
    private final byte opcode;

    /**
     * Create a new opcode for an instruction. Default to no arguments.
     * @param opcode The opcode
     */
    CAsm(int opcode) {
        this.opcode = (byte) opcode;
    }

    /**
     * Get the opcode for the instruction.
     * @return the opcode value
     */
    public byte opcode() {
        return(this.opcode);
    }

    /**
     * Emit code for the opcode
     * @param fp The output port to send the opcode to
     */
    public void emit(OutputPort fp) {
        fp.writeb(opcode);
    }

    /**
     * Write a long to the output port. Fixnums are serialised as octets with groups of 7 bits.
     * The last octet should have 1 in the high bit, all other octets should have 0 in the high bit.
     * @param p The output port to write to
     * @param val The value to write
     */
    public static void writeLong(OutputPort p, long val) {
        // Loop while there are still more than 7 bits left in the number
        while (val < -64 || val > 63) {
            // Write seven bits of the number, leaving the MSB 0
            p.writeb((int) (val & 0x7f));
            val >>= 7;
        }
        // Write the last octet with the remaining bits, setting the MSB to 1
        p.writeb((int) ((val & 0x7f) | 0x80));
    }

    /**
     * Write a double to the output port. Writes things out in IEEE-754 double precision format
     * @param p The output port to write to
     * @param val The value to write
     */
    public static void writeDouble(OutputPort p, double val) {
        long raw = Double.doubleToRawLongBits(val);
        for (int i=0; i<8; i++) {
            p.writeb((int) (raw & 0xff));
            raw >>= 8;
        }
    }

    /**
     * Write a UTF-8 string into the output port. Prefixes the length.
     * @param p The port to write to
     * @param str the string
     */
    public static void writeString(OutputPort p, String str) {
        writeLong(p, str.length());
        str.chars().forEach(ch -> p.writec(Rune.get(ch)));
    }
}

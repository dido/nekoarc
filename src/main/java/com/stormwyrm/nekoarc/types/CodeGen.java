package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.vm.VirtualMachine;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

public class CodeGen extends ArcObject {
    public static final ArcObject TYPE = Symbol.intern("code");

    private ByteArrayOutputStream geninst;
    private Vector<ArcObject> genlits;

    public CodeGen() {
        geninst = new ByteArrayOutputStream();
        genlits = new Vector<>();
    }

    public void load(VirtualMachine vm, int ip) {
        vm.load(geninst.toByteArray(), (ArcObject[])genlits.toArray(), ip);
    }

    public int emits(byte op, byte... vals) {
        int pos = geninst.size();
        geninst.write(op);
        for (byte val : vals)
            geninst.write(val);
        return(pos);
    }

    public int emit(byte op, int... vals) {
        int pos = geninst.size();
        geninst.write(op);
        for (int val : vals) {
            for (int i=0; i<4; i++) {
                geninst.write(val & 0xff);
                val >>= 8;
            }
        }
        return(pos);
    }

    public int literal(ArcObject lit) {
        int size = genlits.size();
        genlits.addElement(lit);
        return(size);
    }

    @Override
    public ArcObject type() {
        return(TYPE);
    }
}

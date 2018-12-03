package com.stormwyrm.nekoarc.vm;

import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.functions.*;
import com.stormwyrm.nekoarc.functions.arith.Add;
import com.stormwyrm.nekoarc.functions.io.*;
import com.stormwyrm.nekoarc.functions.list.*;
import com.stormwyrm.nekoarc.functions.typehandling.*;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.util.ObjectMap;

public class VirtualMachine {
    public final CodeGen cg;
    private byte[] code;
    private  ArcObject[] literals;
    public ObjectMap<Symbol, ArcObject> genv = new ObjectMap<>();

    public VirtualMachine() {
        cg = new CodeGen();
        code = null;
    }

    public byte[] code() {
        return(code);
    }

    public void load() {
        cg.load(this);
    }

    public void load(final byte[] instructions, final ArcObject[] literals) {
        this.code = instructions;
        this.literals = literals;
    }

    public void load(final byte[] instructions) {
        load(instructions, null);
    }

    public ArcObject literal(int offset) {
        return (literals[offset]);
    }// add or replace a global binding

    public ArcObject bind(Symbol sym, ArcObject binding) {
        genv.put(sym, binding);
        return (binding);
    }

    public ArcObject defbuiltin(Builtin builtin) {
        bind((Symbol) Symbol.intern(builtin.getName()), builtin);
        return (builtin);
    }

    public ArcObject value(Symbol sym) {
        if (!genv.containsKey(sym))
            throw new NekoArcException("Unbound symbol " + sym);
        return (genv.get(sym));
    }

    public void initSyms() {

        // Type handling (5/5)
        defbuiltin(Type.getInstance());
        defbuiltin(Annotate.getInstance());
        defbuiltin(Rep.getInstance());
        defbuiltin(Sym.getInstance());
        defbuiltin(Coerce.getInstance());

        // Predicates
        defbuiltin(LessThan.getInstance());
        defbuiltin(GreaterThan.getInstance());
        defbuiltin(LessThanOrEqual.getInstance());
        defbuiltin(GreaterThanOrEqual.getInstance());
        defbuiltin(Spaceship.getInstance());
        defbuiltin(Exact.getInstance());
        defbuiltin(Is.getInstance());
        defbuiltin((Iso.getInstance()));

        // List Operations (7/7)
        defbuiltin(Car.getInstance());
        defbuiltin(Cdr.getInstance());
        defbuiltin(Cadr.getInstance());
        defbuiltin(Cddr.getInstance());
        defbuiltin(FCons.getInstance());
        defbuiltin(Scar.getInstance());
        defbuiltin(Scdr.getInstance());

        // Math Operations
        defbuiltin(Add.getInstance());

        // Tables
        defbuiltin(FTable.getInstance());
        defbuiltin(MapTable.getInstance());

        // Strings
        defbuiltin(NewString.getInstance());

        // Miscellaneous
        defbuiltin(Len.getInstance());
        defbuiltin(SRef.getInstance());
        defbuiltin(Bound.getInstance());

        // Basic I/O primitives
        defbuiltin(ReadB.getInstance());
        defbuiltin(ReadC.getInstance());
        defbuiltin(UngetC.getInstance());
        defbuiltin(PeekC.getInstance());
        defbuiltin(FInString.getInstance());

        // Error handling and continuations
        defbuiltin(CCC.getInstance());
    }

    public ArcObject boundP(ArcObject arg) {
        if (!(arg instanceof Symbol))
            throw new NekoArcException("bound expected symbol, given " + arg);
        if (genv.containsKey((Symbol) arg))
            return (True.T);
        return (Nil.NIL);
    }
}
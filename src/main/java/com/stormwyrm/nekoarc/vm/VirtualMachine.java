/*  Copyright (C) 2018 Rafael R. Sevilla

    This file is part of NekoArc

    NekoArc is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 3 of the
    License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
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
import com.stormwyrm.nekoarc.types.ArcThread;
import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.types.Symbol;
import com.stormwyrm.nekoarc.util.ObjectMap;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * NekoArc virtual machine.
 */
public class VirtualMachine {
    public final CodeGen cg;
    private byte[] code;
    private  ArcObject[] literals;
    /**
     * Lock protecting the global environment
     */
    private final ReentrantReadWriteLock genvlock = new ReentrantReadWriteLock(true);
    private final ObjectMap<Symbol, ArcObject> genv = new ObjectMap<>();

    /**
     * Create a new virtual machine from a code generator
     * @param cg the code generator to load from
     */
    public VirtualMachine(CodeGen cg) {
        this.cg = cg;
        cg.load(this);
    }

    /**
     * Create a new virtual machine with an empty code generator
     */
    public VirtualMachine() {
        cg = new CodeGen();
        code = null;
    }

    /**
     * Get the bytecode in this virtual machine
     * @return the code
     */
    public byte[] code() {
        return(code);
    }

    /**
     * Load bytecode and literals into the virtual machine
     * @param instructions bytecode to load
     * @param literals literal data to load
     */
    public void load(final byte[] instructions, final ArcObject[] literals) {
        this.code = instructions;
        this.literals = literals;
    }

    /**
     * Load the vm from the internal code generator
     */
    public void load() {
        cg.load(this);
    }

    /**
     * Load explicit bytecode into the virtual machine
     * @param instructions the bytecode to load
     */
    public void load(final byte[] instructions) {
        load(instructions, null);
    }


    /**
     * Get a literal from the data section of the virtual machine
     * @param offset the offset to get the data from
     * @return the data object at the offset
     */
    public ArcObject literal(int offset) {
        return (literals[offset]);
    }

    /**
     * Bind a global symbol in the virtual machine
     * @param sym the symbol to bind
     * @param binding the value to bind to the symbol
     * @return the binding value
     */
    public ArcObject bind(Symbol sym, ArcObject binding) {
        genvlock.writeLock().lock();
        try {
            genv.put(sym, binding);
        } finally {
            genvlock.writeLock().unlock();
        }
        return(binding);
    }

    /**
     * Check the binding of a global symbol
     * @param arg the symbol to check binding of
     * @return t if symbol is bound, nil if not bound
     */
    public ArcObject boundP(ArcObject arg) {
        genvlock.readLock().lock();
        try {
            if (!(arg instanceof Symbol))
                throw new NekoArcException("bound expected symbol, given " + arg);
            if (genv.containsKey((Symbol) arg))
                return (True.T);
            return (Nil.NIL);
        } finally {
            genvlock.readLock().unlock();
        }
    }

    /**
     * Get the binding of a global symbol
     * @param sym the symbol get value of
     * @return the binding
     */
    public ArcObject value(Symbol sym) {
        genvlock.readLock().lock();
        try {
            if (!genv.containsKey(sym))
                throw new NekoArcException("Unbound symbol " + sym);
            return(genv.get(sym));
        } finally {
            genvlock.readLock().unlock();
        }
    }


    /**
     * Define a builtin
     * @param builtin the builtin to define
     * @return the builtin bound
     */
    public ArcObject defbuiltin(Builtin builtin) {
        bind((Symbol) Symbol.intern(builtin.getName()), builtin);
        return(builtin);
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
        defbuiltin(FOutString.getInstance());
        defbuiltin(Inside.getInstance());
        defbuiltin(FInFile.getInstance());
        defbuiltin(FOutFile.getInstance());
        defbuiltin(FSeek.getInstance());
        defbuiltin(FTell.getInstance());

        // Error handling and continuations
        defbuiltin(CCC.getInstance());
        defbuiltin(DynamicWind.getInstance());
        defbuiltin(Err.getInstance());
        defbuiltin(OnErr.getInstance());
        defbuiltin(Details.getInstance());
    }

    public ArcThread spawn(ArcThread t) {
        t.thread = new Thread(t);
        t.thread.start();
        return(t);
    }

    public ArcThread spawn(int ip) {
        ArcThread t = new ArcThread(this);
        t.setIP(ip);
        return(spawn(t));
    }
}
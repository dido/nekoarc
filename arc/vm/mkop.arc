;; Copyright (C) 2018 Rafael R. Sevilla
;;
;; This file is part of NekoArc
;;
;; NekoArc is free software; you can redistribute it and/or modify it
;; under the terms of the GNU Lesser General Public License as
;; published by the Free Software Foundation; either version 3 of the
;; License, or (at your option) any later version.
;;
;; This library is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU Lesser General Public License for more details.
;;
;; You should have received a copy of the GNU Lesser General Public
;; License along with this library; if not, see <http://www.gnu.org/licenses/>
;;
;; Generate Op.java from instructions.arc
;;
(def genop (inf outf)
  (w/outfile ofp outf
	     (w/stdout ofp
	       (prn "
/* This file is automatically generated -- DO NOT EDIT! */
package com.stormwyrm.nekoarc;

import com.stormwyrm.nekoarc.types.CodeGen;
import com.stormwyrm.nekoarc.vm.VirtualMachine;

public enum Op {")
	       (w/infile fp inf
		 (whilet inst (read fp)
		   (let (itype mnemonic opcode . extra) inst
		     (pr "    "(if (no mnemonic)
				   "NIL"
				   (upcase mnemonic))
			 "(0x" (coerce opcode 'string 16))
		     (case itype
		       definsl (pr ", ArgType.LLABEL_ARGS")
		       definsr (pr ", ArgType.REG_ARGS")
		       definss (pr ", ArgType.SMALL_ARGS")
		       definsc (pr ", ArgType.CLABEL_ARGS"))
		     (prn "),"))))
		 (prn "    LAST(0xff);

    /**
     * Argument types
     */
    private enum ArgType {
        /** No arguments */
        NONE,
        /** Regular arguments (32-bit), label not permitted */
        REG_ARGS,
        /** Regular arguments (32-bit), can use code labels */
        CLABEL_ARGS,
        /** Regular arguments (32-bit), can use literal labels */
        LLABEL_ARGS,
        /** Small arguments (8-bit) */
        SMALL_ARGS
    }

    /**
     * The opcode for the instruction
     */
    private final byte opcode;
    /**
     * The type of the argument
     */
    private final ArgType argType;

    /**
     * Create a new opcode for an instruction
     * @param opcode The opcode to generate
     * @param t The instruction type
     */
    Op(int opcode, ArgType t) {
        this.opcode = (byte) opcode;
        this.argType = t;
    }

    /**
     * Create a new opcode for an instruction. Default to no arguments.
     * @param opcode The opcode
     */
    Op(int opcode) {
        this(opcode, ArgType.NONE);
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
     * @param cg The code generator to emit to
     * @param args the arguments to the opcode
     * @return the code generator offset at which the instruction was emitted
     */
    public int emit(CodeGen cg, int... args) {
        // Determine the count of arguments from top two bits of opcode
        int argcount = (opcode >> 6) & 0x03;
        if (argcount != args.length) {
            throw new NekoArcException(\"wrong number arguments for instruction \" + this.name()
                    + \" (\" + args.length + \" given, \" + argcount + \" required\");
        }
        if (argType == ArgType.SMALL_ARGS)
            return(cg.emits(opcode, args));
        return(cg.emit(opcode, args));
    }

    /**
     * Emit code for the opcode with a label
     * @param cg The code generator to emit to
     * @param label The label argument
     * @return The code generator offset at which the instruction was emitted
     */
    public int emit(CodeGen cg, String label) {
        int pos;
        // Determine what type of label to use
        switch (argType) {
            case CLABEL_ARGS:
                pos = cg.bremit(opcode, label);
                break;
            case LLABEL_ARGS:
                pos = cg.lemit(opcode, label);
                break;
            default:
                throw new NekoArcException(\"instruction \" + this.name() + \" cannot use label arguments\");
        }
        return(pos);
    }


    @Deprecated
    public int emit(VirtualMachine vm, int... args) {
        return(emit(vm.cg, args));
    }
}"))))


;; Copyright (C) 2019 Rafael R. Sevilla
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
;; This is required for bootstrapping only!
;;

(= instructions (table))

(def mkinst (typ inst opcode (o args))
  (= (instructions inst) (list opcode typ (len args))))

(mac definst (inst opcode . body)
  `(mkinst 'noarg ',inst ,opcode))

(mac definsl (inst opcode args . body)
  `(mkinst 'llabel ',inst ,opcode ',args))

(mac definsr (inst opcode args . body)
  `(mkinst 'reg ',inst ,opcode ',args))

(mac definss (inst opcode args . body)
  `(mkinst 'small ',inst ,opcode ',args))

(mac definsc (inst opcode args . body)
  `(mkinst 'clabel ',inst ,opcode ',args))

;; Generate instruction table above from macros

(load "vm/instructions.arc")

;; Create a new code generation context
;; This is basically a list with the following elements:
;; 0 - Code, emitted code instruction list.
;;     These can be bare instruction mnemonics, lists starting with a
;;     mnemonic with its arguments or lists starting with a label
;;     followed by a mnemonic and any arguments.
;; 1 - Literal data
;;     This consists of an integer index and two hash tables, one of
;;     which is indexed by the integer, and gives a pair with the
;;     type and string representation of the data, and the other
;;     is indexed by the string representation of the data, and returns
;;     its integer index.
;; 2 - Literal labels
;;     Hash table indexed by labels, giving index number into literal data
;;     corresponding to the label, or nil if the label has been used but
;;     not (yet) defined.
(def codegen ()
  (list nil				; Code
	(list 0 (table) (table))	; Literal Data
	(table)))			; Literal Labels

(def gen-inst (ctx label inst args)
  (if (no label)
      (if (no args)
	  (= (ctx 0) (+ (ctx 0) `(,inst)))
	  (= (ctx 0) (+ (ctx 0) `((,inst ,@args)))))
      (= (ctx 0) (+ (ctx 0) `((,label ,inst ,@args))))))

(def xlemit (ctx label inst . args)
  (let (opcode typ nargs) (instructions inst)
    (if (no opcode)
	(err "unknown instruction " inst))
    (if (no (is (len args) nargs))
	(err (+ "instruction " inst " takes " (string nargs) " arguments, " (string (len args)) " args given"))
    (if (is typ 'noarg)
	(gen-inst ctx label inst)
	(in typ 'llabel 'clabel)
	(if (no (all [is (type _) 'sym] args))
	    (err (+ "instruction " (string inst) " takes label args, non-label found in " args))
	    (gen-inst ctx label inst args))
	(in typ 'reg 'small)
	(if (no (all [is (type _) 'int] args))
	    (err (+ "instruction " (string inst) " takes num args, non-num found in " (string args)))
	    (if (and (is typ 'small) (some [> _ 255] args))
		(err (+ "instruction " (string inst) " takes small args, arg too big found in " (string args)))
		(gen-inst ctx label inst args)))
	(err (+ "internal error, unknown type " (string typ) " for instruction " inst))))))

(mac emit (ctx inst . args)
  (let qargs (map (fn (x) `(quote ,x)) args)
    `(xlemit ,ctx nil ',inst ,@qargs)))

(mac label (ctx label inst . args)
  (let qargs (map (fn (x) `(quote ,x)) args)
    `(xlemit ,ctx ',label ',inst ,@qargs)))


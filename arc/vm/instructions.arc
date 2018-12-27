m;; Copyright (C) 2018 Rafael R. Sevilla
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
;; Instructions are defined by definst, which takes the instruction mnemonic,
;; the opcode, and a list of names for arguments.
;;
;; There are several built-in functions that manipulate various
;; aspects of the virtual machine:
;;
;; * acc - The accumulator. When an argument is applied to it, sets the
;;         value of the that argument to it.
;; * push - Push its argument onto the stack
;; * pop - Pop the stack and return its value.
;; * literal - get a literal from literal space at the offset argument
;;

(definst nop 0x00 ())

(definst push 0x01 ()
	 (push acc))

(definst pop 0x02 ()
	 (acc (pop)))

(definst ret 0x0d ()
	 (restorecont))

(definst no 0x11 ()
 	 (acc (if (is acc nil) t nil)))

(definst true 0x12 ()
	 (acc t))

(definst nil 0x13 ()
	 (acc nil))

(definst hlt 0x14 ()
	 (halt))

(definst add 0x15 ()
	 (acc (+ (pop) acc)))

(definst sub 0x16 ()
	 (acc (- (pop) acc)))

(definst mul 0x17 ()
	 (acc (* (pop) acc)))

(definst div 0x18 ()
	 (acc (/ (pop) acc)))

(definst cons 0x19 ()
	 (acc (cons (pop) acc)))

(definst car 0x1a ()
	 (acc (car acc)))

(definst cdr 0x1b ()
	 (acc (cdr acc)))

(definst scar 0x1c ()
	 (let arg1 (pop)
	   (scar arg1 acc)
	   (acc arg1)))

(definst scdr 0x1d ()
	 (let arg1 (pop)
	   (scdr arg1 acc)
	   (acc arg1)))

(definst is 0x1f ()
	 (acc (is (pop) acc)))

(definst consr 0x24 ()
	 (acc (cons acc (pop))))

(definst dcar 0x26 ()
	 (acc (if (or (nilp acc) (unboundp acc))
		  unbound
		  (car acc))))

(definst dcdr 0x27
	 (acc (if (or (nilp acc) (unboundp acc))
		  unbound
		  (cdr acc))))

(definst ldl 0x43 (offset)
	 (acc (literal offset)))

(definst ldi 0x44 (value)
	 (acc value))

(definst ldg 0x45 (offset)
	 (acc (value (literal offset))))

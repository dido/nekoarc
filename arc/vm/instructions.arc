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

(definst nop #x00 ())

(definst push #x01 ()
	 (push acc))

(definst pop #x02 ()
	 (acc (pop)))

(definst ret #x0d ()
	 (restorecont))

(definst no #x11 ()
 	 (acc (if (is acc nil) t nil)))

(definst true #x12 ()
	 (acc t))

(definst nil #x13 ()
	 (acc nil))

(definst hlt #x14 ()
	 (halt))

(definst add #x15 ()
	 (acc (+ (pop) acc)))

(definst sub #x16 ()
	 (acc (- (pop) acc)))

(definst mul #x17 ()
	 (acc (* (pop) acc)))

(definst div #x18 ()
	 (acc (/ (pop) acc)))

(definst cons #x19 ()
	 (acc (cons (pop) acc)))

(definst car #x1a ()
	 (acc (car acc)))

(definst cdr #x1b ()
	 (acc (cdr acc)))

(definst scar #x1c ()
	 (let arg1 (pop)
	   (scar arg1 acc)
	   (acc arg1)))

(definst scdr #x1d ()
	 (let arg1 (pop)
	   (scdr arg1 acc)
	   (acc arg1)))

(definst is #x1f ()
	 (acc (is (pop) acc)))

(definst consr #x24 ()
	 (acc (cons acc (pop))))

(definst dcar #x26 ()
	 (acc (if (or (nilp acc) (unboundp acc))
		  unbound
		  (car acc))))

(definst dcdr #x27
	 (acc (if (or (nilp acc) (unboundp acc))
		  unbound
		  (cdr acc))))

(definsl ldl #x43 (offset)
	 (acc (literal offset)))

(definsr ldi #x44 (value)
	 (acc value))

(definsl ldg #x45 (offset)
	 (acc (value (literal offset))))

(definsl stg #x46 (offset)
	 (bind (value (literal offset)) acc))

(definsl ldlp #x47 (offset)
	 (push (acc (literal offset))))

(definsr ldip #x48 (value)
	 (push (acc value)))

(definsl ldgp #x49 (value)
	 (push (acc (value (literal offset)))))

(definss apply #x4c (value)
	 (argc value)
	 (apply acc))

(definsc cls #x4d (target)
	 (acc (closure (+ target ip))))

(definsc jmp #x4e (target)
	 (setip (+ ip target)))

(definsc jt #x4f (target)
	 (if (no (nilp acc))
	     (setip (+ ip target))))

(definsc jf #x50 (target)
	 (if (nilp acc)
	     (setip (+ ip target))))

(definsc jbnd #x51 (target)
	 (if (boundp acc)
	     (setip (+ ip target))))

(definsc cont #x52 (target)
	 (cont target))

(definss menv #x65 (count)
	 (menv count))

(definss lde0 #x69 (index)
	 (acc (getenv 0 index)))

(definss ste0 #x6a (index)
	 (setenv 0 index acc))

(definss lde0p #x6b (index)
	 (push (acc (getenv 0 index))))

(definss lde #x87 (depth index)
	 (acc (getenv depth index)))

(definss ste #x88 (depth index)
	 (setenv depth index acc))

(definss ldep #x89 (depth index)
	 (push (acc (getenv depth index))))

(definss env #xca (min ds opt)
	 (argcheck min (+ min opt))
	 (mkenv argc (+ (- (+ min opt) argc) ds)))

(definss envr #xcb (min ds opt)
	 (argcheck min -1)
	 (mkenvrest argc min ds opt))


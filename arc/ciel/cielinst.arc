;; Copyright (C) 2018, 2019 Rafael R. Sevilla
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

;; Push a nil onto the stack
(definst gnil #x00)

;; Push a true onto the stack
(definst gtrue #x08)

;; Push a fixnum onto the stack
(definst gfix #x10)

;; Push a flonum onto the stack
(definst gflo #x18)

;; Push a Unicode rune onto the stack
(definst grune #x20)

;; ;; Push a string onto the stack
;; (definst gstr #x30)

;; ;; Push a symbol onto the stack
;; (definst gsym #x38)

;; ;; Cons the two top elements on the stack
;; (definst ccons #x80)

;; ;; Annotate with the two top elements on the stack
;; (definst cann #x81)

;; ;; Push a vector of a certain length given by the argument
;; ;; onto the stack
;; (definst gvec #x90)

;; ;; Push an empty hash table onto the stack
;; (definst gtab #x91)

;; ;; Set an element of a cons or a vector with the index
;; (definst xset #xa0)

;; (definst xtset #xa1)

;; (definst mput #xc0)

;; (definst mget #xc1)

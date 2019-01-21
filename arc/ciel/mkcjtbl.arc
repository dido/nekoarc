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
;; Generate CielJmpTbl.java from cielinst.arc
;;
(def genjmptbl (inf outf)
  (let itbl (table)
    (w/infile fp inf
      (whilet inst (read fp)
	(let (itype mnemonic opcode . extra) inst
	  (= (itbl opcode) (upcase (coerce mnemonic 'string))))))
    (w/outfile ofp outf
	       (w/stdout ofp
		 (prn "
/* This file is automatically generated -- DO NOT EDIT! */
package com.stormwyrm.nekoarc.ciel;

import com.stormwyrm.nekoarc.ciel.instruction.*;

public class CielJmpTbl {
    private static final CINVALID NOINST = new CINVALID();
    public static final CielInstruction[] jmptbl = {")
		 (for i 0 255
		      (pr "            ")
		      (aif (itbl i)
			   (prn "new " it "(),        // 0x" (coerce i 'string 16))
			   (prn "NOINST,")))
		 (prn "    };
}")))))



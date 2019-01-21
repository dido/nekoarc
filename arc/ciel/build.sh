#!/bin/sh
# Copyright (C) 2018, 2019 Rafael R. Sevilla
#
# This file is part of NekoArc
#
# NekoArc is free software; you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation; either version 3 of the
# License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, see <http://www.gnu.org/licenses/>.
#
ARC=$HOME/bin/arc
echo -n "Generatng CAsm.java..."
cat <<EOF | $ARC
(load "mkcasm.arc")
(gencasm "cielinst.arc" "../../src/main/java/com/stormwyrm/nekoarc/ciel/CAsm.java")
EOF
echo "done"
echo -n "Generating CielJmpTbl.java..."
cat <<EOF | $ARC
(load "mkcjtbl.arc")
(genjmptbl "cielinst.arc" "../../src/main/java/com/stormwyrm/nekoarc/ciel/CielJmpTbl.java")
EOF
echo "done"

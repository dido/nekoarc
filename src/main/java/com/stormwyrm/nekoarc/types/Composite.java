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
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.True;
import com.stormwyrm.nekoarc.util.ObjectMap;

/**
 * Superclass of all composite objects (vectors, conses, and tables). Defines several common
 * tasks
 */
public abstract class Composite extends ArcObject {
    /**
     * Visit the object itself. Should be called by all subclasses before visiting any objects inside the
     * composite.
     * @param seen The seen hash
     * @param counter The counter for visited items
     * @return True if 'this' was already visited before.
     */
    protected boolean visitThis(ObjectMap<ArcObject, ArcObject> seen, int[] counter) {
        if (seen.containsKey(this)) {
            ArcObject val = seen.get(this);
            if (Nil.NIL.is(val)) {
                seen.put(this, new Cons(Fixnum.get(counter[0]), Nil.NIL));
                counter[0] += 1;
            }
            return(true);
        }
        seen.put(this, Nil.NIL);
        return(false);
    }

    /**
     * Check to see if the object is already referenced in the seen hash. If it is, the caller
     * should return true, and then return sb which has been set to the reference value. The function
     * may also update sb with a sharpsign-equals prefix if it is referenced somewhere.
     * @param seen The seen hash
     * @param sb A StringBuilder that is used
     * @return True if the object is already referenced
     */
    protected boolean checkReferences(ObjectMap<ArcObject, ArcObject> seen, StringBuilder sb) {
        ArcObject s=Nil.NIL;
        if (seen.containsKey(this) && !Nil.NIL.is(s = seen.get(this)) && !Nil.NIL.is(s.cdr())) {
            sb.append("#");
            sb.append(s.car().toString());
            sb.append("#");
            return(true);
        }
        if (!Nil.NIL.is(s)) {
            sb.append("#");
            sb.append(s.car().toString());
            sb.append("=");
            s.scdr(True.T);
        }
        return(false);
    }

    /**
     * Get the string representation of the composite. Uses the visit method to visit the object
     * and its children to generate a seen hash, that is used to generate string representation.
     * @return The string representation of the vector.
     */
    @Override
    public String toString() {
        ObjectMap<ArcObject, ArcObject> seen = new ObjectMap<>();
        this.visit(seen, new int[]{0});
        return(toString(seen));
    }

    protected class OOB extends RuntimeException { }

    /**
     * Iso for composites, with seen hash. Subclasses should call this and return its value, but catch
     * the OOB exception and do further testing as required.
     * @param other the object to compare with
     * @param seen the seen hash
     * @return True if the objects are structurally equal.
     */
    @Override
    public boolean iso(ArcObject other, ObjectMap<ArcObject, ArcObject> seen) {
        // if the seen hash is set for one but not the other, they obviously can't be the same
        if (seen.containsKey(this) ^ seen.containsKey(other))
            return(false);
        // If the seen hash is set for both, then they are not different thus far
        if (seen.containsKey(this) && seen.containsKey(other))
            return(true);
        seen.put(this, True.T);
        seen.put(other, True.T);
        // We can't determine iso based only on the seen hashes. Do further tests.
        throw new OOB();
    }

    /**
     * Iso for composites. Will just call the iso with seen using a new seen hash.
     * @param other the object to compare with
     * @return True if the objects are structurally equivalent.
     */
    @Override
    public boolean iso(ArcObject other) {
        return(iso(other, new ObjectMap<>()));
    }
}

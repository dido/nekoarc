/*
 * Copyright (c) 2019 Rafael R. Sevilla
 *
 * This file is part of NekoArc
 *
 * NekoArc is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.stormwyrm.nekoarc.types;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.NekoArcException;
import com.stormwyrm.nekoarc.Nil;
import com.stormwyrm.nekoarc.ciel.Ciel;
import com.stormwyrm.nekoarc.util.Callable;
import com.stormwyrm.nekoarc.util.CallSync;
import com.stormwyrm.nekoarc.util.ObjectMap;

/**
 * The base class of all Arc Objects. Defines many basic methods.
 */
public abstract class ArcObject implements Callable {
	private final CallSync caller = new CallSync();

	/**
	 * Take the car of the object if possible.
	 * @return the car of the object
	 */
	public ArcObject car() {
		throw new NekoArcException("Can't take car of " +
				this.type());
	}

	/**
	 * Take the cdr of the object if possible.
	 * @return the cdr of the object
	 */
	public ArcObject cdr() {
		throw new NekoArcException("Can't take cdr of " +
				this.type());
	}

	/**
	 * Set the cdr of the object
	 * @param ncar The new value of the object's car
	 * @return usually ncar
	 */
	public ArcObject scar(ArcObject ncar) {
		throw new NekoArcException("Can't set car of " + this.type());

	}

	/**
	 * Set the cdr of the object
	 * @param ncar The new value of the object's cdr
	 * @return usually ncdr
	 */
	public ArcObject scdr(ArcObject ncar) {
		throw new NekoArcException("Can't set cdr of " + this.type());
	}

	/**
	 * Set the value of a reference. This corresponds to the sref builtin
	 * function and is generally used to set values in aggregate objects
	 * such as lists, vectors, and hash tables, and can be used to change
	 * characters in strings.
	 * @param value The new value to assign to the index
	 * @param index The index
	 * @return the new value assigned
	 */
	public ArcObject sref(ArcObject value, ArcObject index)  {
		throw new NekoArcException("Can't sref " + this
				+ "(a " + this.type() + "), other args were "
				+ value + ", " + index);
	}

	/**
	 * Add two ArcObjects if this is meaningful
	 * @param other The value to add to this
	 * @return The sum
	 */
	public ArcObject add(ArcObject other) {
		throw new NekoArcException("add not implemented for "
				+ this.type() + " " + this);
	}

	/**
	 * Get the length of the ArcObject if this is meaningful
	 * @return the length
	 */
	public long len() {
		throw new NekoArcException("len: expects one string, vector,"
				+ " list, or hash argument, cannot take length of "
				+ this + " (" + this.type() + ")");
	}

	/**
	 * Get the type of the ArcObject. This should normally be a symbol giving
	 * the type name.
	 * @return the type name.
	 */
	public abstract ArcObject type();

	/**
	 * Get the representation of the ArcObject. This is normally the object
	 * itself, except for Annotated objects, which have their internal rep.
	 * @return the representation
	 */
	public ArcObject rep() {
		return(this);
	}

	/**
	 * The number of required arguments if the object can be applied.
	 * @return argument count
	 */
	public int requiredArgs() {
		throw new NekoArcException("Cannot invoke object of type " + type());

	}

	/**
	 * The number of optional arguments if the object can be applied.
	 * @return argument count
	 */
	public int optionalArgs() {
		return(0);
	}

	/**
	 * The number of extra arguments if the object can be applied.
	 * @return argument count
	 */
	public int extraArgs() {
		return(0);
	}

	/**
	 * Determine whether object can be applied variadic. If this is true,
	 * the interpreter will place any extra arguments into a list after the
	 * last named optional parameter.
	 * @return True if the object can be applied variadic
	 */
	public boolean variadicP() {
		return(false);
	}

	/**
	 * Is the data type an exact value. As of now only Fixnums are exact.
	 * @return True if the data type is exact
	 */
	public boolean exactP() { return(false); }

	/**
	 * The basic apply. This should normally not be overridden. Only Closure should probably override it
	 * because it runs completely within its ArcThread. This will create an invoke thread to run the Java
	 * code, suspending the thread that called it until the invoke thread terminates and returns its value.
	 *
	 * @param thr The thread applying the object
	 * @param caller The function calling
	 */
	public void apply(ArcThread thr, Callable caller) throws Throwable {
		int minenv, dsenv, optenv;
		minenv = requiredArgs();
		dsenv = extraArgs();
		optenv = optionalArgs();
		if (variadicP()) {
			int i;
			thr.argcheck(minenv, -1);
			ArcObject rest = Nil.NIL;
			for (i = thr.argc(); i>(minenv + optenv); i--)
				rest = new Cons(thr.pop(), rest);
			thr.mkenv(i, minenv + optenv - i + dsenv + 1);
			/* store the rest parameter */
			thr.setenv(0, minenv + optenv + dsenv, rest);
		} else {
			thr.argcheck(minenv, minenv + optenv);
			thr.mkenv(thr.argc(), minenv + optenv - thr.argc() + dsenv);
		}

		// Start the invoke thread
		InvokeThread ithr = new InvokeThread(thr, caller, this);
		new Thread(ithr).start();

		// Suspend the caller's thread until the invoke thread returns
		ArcObject retval = caller.sync().retval();
		if (retval instanceof AException)
			throw ((AException) retval).exception;
		thr.setAcc(retval);
	}

	/**
	 * Invoke the object.
	 * @param ithr The invocation thread.
	 * @return The return value
	 */
	public ArcObject invoke(InvokeThread ithr) throws Throwable {
		throw new NekoArcException("Cannot invoke object of type " + type());

	}

    /**
     * Convert object to a string with a seen hash. Usually used only for composite objects
     * @param seen the seen hash
     * @return The string representation of the object
     */
	public String toString(ObjectMap<ArcObject,ArcObject> seen) { return(toString()); }

	/**
	 * Convert the object to a string
	 * @return The string representation of the object
	 */
	public String toString() {
		return("#<" + type() + ":" + this.hashCode() + ">");
	}

	// default implementation

	/**
	 * Shallow compare the object with another. For atomic types this is generally
	 * a reference comparison.
	 * @param other The object to compare
	 * @return true if the objects are equivalent
	 */
	public boolean is(ArcObject other) {
		return(this == other);
	}

    /**
     * Deep compare the object with another with a seen hash. The default implementation just
     * calls iso discarding the seen hash.
     * @param other the object to compare with
     * @param seen the seen hash
     * @return true if the objects are structurally equivalent.
     */
	public boolean iso(ArcObject other, ObjectMap<ArcObject, ArcObject> seen) {
	    return(iso(other));
    }


	/**
	 * Visit the object. Default implementation does nothing. Only used by composite objects
	 * like conses and vectors.
	 * @param seen The seen hash
	 */
	public void visit(ObjectMap<ArcObject, ArcObject> seen, int[] counter) { }

	/**
	 * Deep compare the object with another. The default implementation is the
	 * same as the 'is' method above, unless overridden by a subclass.
	 * @param other the object to compare with
	 * @return true if the objects are structurally equivalent.
	 */
	public boolean iso(ArcObject other) { return(this.is(other)); }

	/**
	 * sync
	 * @return the caller
	 */
	@Override
	public CallSync sync()
	{
		return(caller);
	}

    /**
     * Coerce an object to a new type
     * @param newtype The new type to convert to
     * @param extra additional parameters for the conversion, if any
     * @return coerced object of new type if coercion is valid
     */
	public ArcObject coerce(ArcObject newtype, ArcObject extra) {
		// Coercing an object to its own type just returns the object itself
		if (newtype.is(this.type()))
			return(this);
		throw new NekoArcException("Cannot coerce " + this + " to " + newtype);
	}


	/**
	 * Marshal object into Ciel format to an output port
	 * @param p The port to write to
	 */
	public void marshal(OutputPort p) {
		throw new NekoArcException("Cannot dump object of type " + this.type());
	}
}

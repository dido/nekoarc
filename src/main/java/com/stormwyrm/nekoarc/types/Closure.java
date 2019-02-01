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

import com.stormwyrm.nekoarc.util.Callable;

/**
 * A closure
 */
public class Closure extends ArcObject {
	public static final ArcObject TYPE = Symbol.intern("closure");

	private final ArcObject env;
	private final Code code;

	/**
	 * Create a new closure.
	 * @param env The environment saved in the closure
	 * @param code The code object.

	 */
	public Closure(ArcObject env, Code code) {
		this.env = env;
		this.code = code;
	}

	/**
	 * Type of the closure
	 * * @return 'closure
	 */
	@Override
	public ArcObject type() {
		return(TYPE);
	}

	/**
	 * Apply a closure. This is the only place where apply should be overriden
	 * @param thr The thread applying the closure
	 * @param caller The function calling
	 */
	@Override
	public void apply(ArcThread thr, Callable caller) {
		ArcObject newenv;
		newenv = this.env;
		thr.setIP(code.ip);
		thr.setenvreg(newenv);
		// If this is not a call from the thr itself, some other additional actions need to be taken.
		// 1. The virtual machine thread should be resumed.
		// 2. The caller must be suspended until the continuation it created is restored.
		if (thr != caller) {
			thr.sync().ret(this);				// wakes the VM so it begins executing the closure
			thr.setAcc(caller.sync().retval());	// sleeps the caller until its own continuation is restored
		}
	}
}

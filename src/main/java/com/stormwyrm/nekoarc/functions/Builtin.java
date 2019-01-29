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
package com.stormwyrm.nekoarc.functions;

import com.stormwyrm.nekoarc.InvokeThread;
import com.stormwyrm.nekoarc.types.ArcObject;
import com.stormwyrm.nekoarc.types.Symbol;

/**
 * Base class for all builtin functions
 */
public abstract class Builtin extends ArcObject {
	public static final Symbol TYPE = (Symbol) Symbol.intern("fn");
	private final String name;
	private final int rargs, eargs, oargs;
	private final boolean variadic;

    /**
     * Create a new builtin
     * @param name Name of the builtin
     * @param req Required arguments for the builtin
     * @param opt Optional arguments for the builtin
     * @param extra Extra arguments for thebuiltin
     * @param va Does the builtin have variable arguments?
     */
	protected Builtin(String name, int req, int opt, int extra, boolean va) {
		this.name = name;
		rargs = req;
		eargs = extra;
		oargs = opt;
		variadic = va;
	}

    /**
     * Create a new builtin
     * @param name Name of thebuiltin
     * @param req Required arguments for the builtin
     */
	protected Builtin(String name, int req) {
		this(name, req, 0, 0, false);
	}

    /**
     * Get the name of builtin
     * @return the name
     */
	public String getName() {
		return(name);
	}

	@Override
	public int requiredArgs() {
		return(rargs);
	}

	public int optionalArgs() {
		return(oargs);
	}

	public int extraArgs() {
		return(eargs);
	}

	public boolean variadicP() {
		return(variadic);
	}

	public abstract ArcObject invoke(InvokeThread ithr) throws Throwable;

	public ArcObject type() {
		return(TYPE);
	}

	@Override
	public String toString() {
		return("#<procedure:" + name +">");
	}
}

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

package com.stormwyrm.nekoarc.util;

import java.util.concurrent.SynchronousQueue;

import com.stormwyrm.nekoarc.types.ArcObject;

public class CallSync
{
	private final SynchronousQueue<ArcObject> syncqueue;

	public CallSync()
	{
		syncqueue = new SynchronousQueue<ArcObject>();
	}

	public void ret(ArcObject retval)
	{
		for (;;) {
			try {
				syncqueue.put(retval);
				return;
			} catch (InterruptedException e) { }
		}
	}

	public ArcObject retval()
	{
		for (;;) {
			try {
				return(syncqueue.take());
			} catch (InterruptedException e) { }
		}
	}
}

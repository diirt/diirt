/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
/*******************************************************************************
 * Copyright (c) 2010-2014 ITER Organization.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.epics.pvmanager.timecache;

import java.util.SortedSet;
import java.util.TreeSet;

import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;

/**
 * A chunk of {@link Data} ordered by {@link Timestamp} with a maximum size.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class DataChunk {

	private SortedSet<Data> datas;
	private TimeInterval interval;

	private final Integer maxData;

	public DataChunk() {
		datas = new TreeSet<Data>();
		// TODO calculate
		this.maxData = 1000;
	}

	/**
	 * Add a data to the chunk.
	 * @param data to be added.
	 * @return <code>true</code> if data have been added, <code>false</code>
	 *         otherwise.
	 */
	public boolean add(Data data) {
		if (isFull() || data == null)
			return false;
		datas.add(data);
		interval = TimeInterval.between(
				datas.first().getTimestamp(), 
				datas.last().getTimestamp());
		return true;
	}

	/** Check if the instance is empty. */
	public boolean isEmpty() {
		return datas.isEmpty();
	}

	/** Check if the instance is full. */
	public boolean isFull() {
		return datas.size() >= maxData;
	}

	public SortedSet<Data> getDatas() {
		return datas;
	}

	public TimeInterval getInterval() {
		return interval;
	}

	@Override
	public String toString() {
		return "DataChunk [datas=" + datas + ", interval=" + interval + "]";
	}

}

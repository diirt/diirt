/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache.query;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.epics.pvmanager.timecache.Data;
import org.epics.pvmanager.timecache.util.CacheHelper;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VType;

/**
 * Represents a chunk of {@link Data} with all {@link Timestamp} within a fixed
 * {@link TimeInterval}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryChunk {

	private static final Logger log = Logger.getLogger(QueryChunk.class.getName());

	private static enum Status {
		NoDataReceived, SomeDataReceived, AllDataReceived, Blank
	}

	private final TimeInterval timeInterval;
	private SortedSet<Data> dataSet = new TreeSet<Data>();
	private Status status = Status.NoDataReceived;

	public QueryChunk(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;
	}

	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

	public boolean addData(Data data) {
		if (data == null || isComplete()
				|| !timeInterval.contains(data.getTimestamp()))
			return false;
		dataSet.add(data);
		status = Status.SomeDataReceived;
		return true;
	}

	public void markComplete() {
		if (this.dataSet.isEmpty()) this.status = Status.Blank;
		else this.status = Status.AllDataReceived;
		log.log(Level.INFO,
				"Completed: " + CacheHelper.format(timeInterval) + ": " + dataSet.size());
	}

	public boolean isComplete() {
		return status.equals(Status.AllDataReceived)
				|| status.equals(Status.Blank);
	}

	public void clearData() {
		this.dataSet.clear();
	}

	public void clearDataAndStatus() {
		this.dataSet.clear();
		this.status = Status.NoDataReceived;
	}

	/**
	 * Transform the chunk to the corresponding {@link QueryData}
	 * implementation.
	 */
	public QueryData toQueryData() {
		switch (this.status) {
		case NoDataReceived:
		case SomeDataReceived:
			// We return only completed chunk
			return new QueryDataNR(timeInterval);
		case AllDataReceived:
			SortedMap<Timestamp, VType> sortedMap = new TreeMap<Timestamp, VType>();
			for (Data data : dataSet)
				// TODO null is a specific case => update status ?
				if (data.getValue() != null)
					sortedMap.put(data.getTimestamp(), data.getValue());
			return new QueryDataComplete(timeInterval, sortedMap);
		default:
			return new QueryDataBlank(timeInterval);
		}
	}
}

/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.ArrayList;
import java.util.List;

import org.epics.pvmanager.timecache.source.DataSource;
import org.epics.pvmanager.timecache.util.CacheHelper;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;

/**
 * Retrieves chunks from the specified {@link DataSource}, channel name and
 * {@link TimeInterval}. Polls chunks from the source until the
 * {@link Timestamp} of the last received {@link Data} is superior to the end of
 * the defined {@link TimeInterval}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class DataRequestThread extends Thread {

	private final String channelName;
	private final DataSource source;
	private TimeInterval interval;
	private Timestamp lastReceived;

	private List<DataRequestListener> listeners;

	public DataRequestThread(String channelName, DataSource source,
			TimeInterval interval) throws Exception {
		if (channelName == null || channelName.isEmpty() || source == null
				|| interval == null)
			throw new Exception("null or empty argument not allowed");
		listeners = new ArrayList<DataRequestListener>();
		this.channelName = channelName;
		this.source = source;
		this.interval = CacheHelper.arrange(interval);
		this.lastReceived = this.interval.getStart();
	}

	/** {@inheritDoc} */
	@Override
	public void run() {
		if (interval.getStart() == null) {
			notifyComplete();
			return;
		}
		DataChunk currentChunk = source.getData(channelName, interval.getStart());
		boolean process = true;
		while (process) {
			if (currentChunk == null || currentChunk.isEmpty()
					|| !CacheHelper.intersects(interval, currentChunk.getInterval())) {
				process = false;
				break;
			} else {
				lastReceived = currentChunk.getInterval().getEnd();
				notifyNewData(currentChunk);
				if (!currentChunk.isFull() || !interval.contains(lastReceived)) {
					process = false;
					break;
				}
			}
			currentChunk = source.getData(channelName, lastReceived);
		}
		notifyComplete();
	}

	// Notify the listeners that a new chunk is available
	private void notifyNewData(DataChunk chunk) {
		for (DataRequestListener l : listeners)
			l.newData(chunk, this);
	}

	// Notify the listeners that the thread has finished requesting samples
	private void notifyComplete() {
		for (DataRequestListener l : listeners)
			l.intervalComplete(this);
	}

	/** Add a {@link DataRequestListener}. */
	public void addListener(DataRequestListener l) {
		if (l != null)
			listeners.add(l);
	}

	/** Remove a {@link DataRequestListener}. */
	public void removeListener(DataRequestListener l) {
		if (l != null)
			listeners.remove(l);
	}

	public String getChannelName() {
		return channelName;
	}

	public DataSource getSource() {
		return source;
	}

	public TimeInterval getInterval() {
		return interval;
	}

	public Timestamp getLastReceived() {
		return lastReceived;
	}

}

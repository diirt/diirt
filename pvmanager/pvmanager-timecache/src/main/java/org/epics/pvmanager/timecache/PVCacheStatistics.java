/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.epics.pvmanager.timecache.util.CacheHelper;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;

/**
 * Statistics of {@link PVCache}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class PVCacheStatistics {

	private class RequestCounter {

		private final Timestamp start;
		private final TimeInterval interval;
		private TimeDuration duration;

		public RequestCounter(TimeInterval interval, Timestamp start) {
			this.interval = interval;
			this.start = start;
		}

		public void intervalCompleted() {
			this.duration = Timestamp.now().durationBetween(start);
		}

		public Timestamp getStart() {
			return start;
		}

		public TimeInterval getInterval() {
			return interval;
		}

		public TimeDuration getDuration() {
			return duration;
		}
	}

	private List<RequestCounter> currentCounters = Collections
			.synchronizedList(new LinkedList<RequestCounter>());
	private List<RequestCounter> finishedCounters = Collections
			.synchronizedList(new LinkedList<RequestCounter>());
	private double sourceRequested = 0;
	private double storageHit = 0;

	public void intervalRequested(TimeInterval interval, Timestamp start) {
		currentCounters.add(new RequestCounter(interval, start));
	}

	public void intervalsCompleted(final TimeInterval interval,
			final Timestamp start) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Iterator<RequestCounter> it = currentCounters.iterator();
				while (it.hasNext()) {
					RequestCounter rc = it.next();
					if (rc.getStart().equals(start)) {
						rc.intervalCompleted();
						it.remove();
						finishedCounters.add(rc);
						break;
					}
				}
			}
		});
		thread.start();
	}

	public void foundStoredData() {
		storageHit++;
	}

	public void sourceRequested() {
		sourceRequested++;
	}

	public String print() {
		StringBuilder sb = new StringBuilder();
		sb.append("# Hit: " + storageHit + "\n");
		sb.append("# Source requested: " + sourceRequested + "\n");
		for (RequestCounter rc : finishedCounters) {
			sb.append("> ");
			sb.append(CacheHelper.format(rc.getInterval()));
			sb.append(" started at ");
			sb.append(CacheHelper.format(rc.getStart()));
			sb.append(": ");
			sb.append(CacheHelper.format(rc.getDuration()));
			sb.append("\n");
		}
		return sb.toString();
	}

}

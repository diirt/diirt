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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.epics.pvmanager.timecache.Data;
import org.epics.pvmanager.timecache.PVCacheImpl;
import org.epics.pvmanager.timecache.PVCacheListener;
import org.epics.pvmanager.timecache.impl.SimpleFileDataSource;
import org.epics.pvmanager.timecache.impl.SimpleMemoryStorage;
import org.epics.pvmanager.timecache.source.DataSource;
import org.epics.pvmanager.timecache.storage.DataStorage;
import org.epics.pvmanager.timecache.util.CacheHelper;
import org.epics.pvmanager.timecache.util.IntervalsList;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link PVCacheImpl}: retrieves samples from sources for a given time
 * interval and stores them.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class PVCacheUnitTest {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static Set<Timestamp> dataTimes = new TreeSet<Timestamp>();
	private static Timestamp start = null;
	private static Timestamp end = null;

	// listener that count data in the requested interval
	private static class TUListener implements PVCacheListener {
		@Override
		public void newData(SortedSet<Data> datas) {
			System.out.println(CacheHelper.format(TimeInterval.between(
					datas.first().getTimestamp(), 
					datas.last().getTimestamp()))
					+ ": " + datas.size());
			for (Data d : datas) {
				if (d.getTimestamp().compareTo(start) >= 0
						&& d.getTimestamp().compareTo(end) <= 0) {
					if (dataTimes.contains(d.getTimestamp())) // doubloon
						System.out.println("D: " + CacheHelper.format(d.getTimestamp()));
					dataTimes.add(d.getTimestamp());
				}
			}
		}
	}

	/**
	 * Test that PV cache retrieves samples in the requested interval and does
	 * not request sources again if asking for the same interval. Test that no
	 * sample is lost when retrieved from storage.
	 */
	@Test
	public void testRetrieve() {
		List<DataSource> sourcesList = new ArrayList<DataSource>();
		sourcesList.add(new SimpleFileDataSource(
				"resources/test/archive-export-singlePV.csv"));
		DataStorage storage = new SimpleMemoryStorage();
		try {
			PVCacheImpl cache = new PVCacheImpl("TEST-BTY0:AI1", sourcesList, storage);
			cache.addListener(new TUListener());

			System.out.println(">>>> first request <<<<");
			dataTimes.clear();
			start = Timestamp.of(dateFormat.parse("2014-04-03 08:00"));
			end = Timestamp.of(dateFormat.parse("2014-04-03 12:00"));
			cache.retrieveDataAsync(TimeInterval.between(start, end));
			Assert.assertTrue(cache.isProcessingSources());
			int limit = 0;
			while (cache.isProcessingSources() && limit <= 60) { // 30s
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				limit++;
			}
			IntervalsList completedIntervals = cache.getCompletedIntervalsList();
			// assert cache has completed the requested interval
			Assert.assertTrue(completedIntervals.contains(TimeInterval.between(start, end)));

			System.out.println(">>>> second request <<<<");
			int firstCount = dataTimes.size();
			dataTimes.clear();

			// ask for same interval and verify no thread is launched
			cache.retrieveDataAsync(TimeInterval.between(start, end));
			Assert.assertFalse(cache.isProcessingSources());
			Assert.assertTrue(cache.isProcessingStorage());
			limit = 0;
			while (cache.isProcessingStorage() && limit <= 60) { // 30s
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				limit++;
			}
			Assert.assertEquals(firstCount, dataTimes.size());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}

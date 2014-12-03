/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import org.diirt.datasource.timecache.DataRequestListener;
import org.diirt.datasource.timecache.DataRequestThread;
import org.diirt.datasource.timecache.DataChunk;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.PVCacheImpl;
import org.diirt.datasource.timecache.PVCacheListener;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.impl.SimpleMemoryStorage;
import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.storage.DataStorage;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.datasource.timecache.util.IntervalsList;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
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
	private static boolean finished = false;

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

	// listener that counts data in the requested interval
	private static class TUListener2 implements DataRequestListener {
		@Override
		public void newData(DataChunk chunk, DataRequestThread thread) {
			System.out.println(CacheHelper.format(TimeInterval.between(
					chunk.getDatas().first().getTimestamp(), 
					chunk.getDatas().last().getTimestamp()))
					+ ": " + chunk.getDatas().size());
			for (Data d : chunk.getDatas()) {
				if (d.getTimestamp().compareTo(start) >= 0
						&& d.getTimestamp().compareTo(end) <= 0) {
					if (dataTimes.contains(d.getTimestamp()))
						System.out.println("D: " + CacheHelper.format(d.getTimestamp()));
					dataTimes.add(d.getTimestamp());
				}
			}
		}

		@Override
		public void intervalComplete(DataRequestThread thread) {
			finished = true;
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
			DataRequestThread thread = cache.retrieveDataAsync(TimeInterval.between(start, end));
			Assert.assertFalse(cache.isProcessingSources());
			thread.addListener(new TUListener2());
			finished = false;
			thread.start();
			limit = 0;
			while (!finished && limit <= 60) { // 30s
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

/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.epics.pvmanager.timecache.Cache;
import org.epics.pvmanager.timecache.CacheFactory;
import org.epics.pvmanager.timecache.CacheImpl;
import org.epics.pvmanager.timecache.PVCache;
import org.epics.pvmanager.timecache.PVCacheImpl;
import org.epics.pvmanager.timecache.query.Query;
import org.epics.pvmanager.timecache.query.QueryData;
import org.epics.pvmanager.timecache.query.QueryImpl;
import org.epics.pvmanager.timecache.query.QueryParameters;
import org.epics.pvmanager.timecache.query.QueryResult;
import org.epics.pvmanager.timecache.util.IntervalsList;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.TimeRelativeInterval;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link CacheImpl}: create a {@link Query} with the related
 * {@link PVCache}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheUnitTest {

	private static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	private static Timestamp start = null;
	private static Timestamp end = null;

	/**
	 * Test that a {@link PVCache} is created when none exists. Test that the
	 * sum of Query.getUpdate samples count is equals to the Query.getResult
	 * samples count. Test that when another query is created with the same
	 * channel name, the same {@link PVCache} is used and the Query.getUpdate &
	 * Query.getResult samples count are the same.
	 */
	@Test
	public void testCreateQuery() {
		try {
			Cache cache = CacheFactory.getCache();
			start = Timestamp.of(dateFormat.parse("2014-04-03 09:00"));
			end = Timestamp.of(dateFormat.parse("2014-04-03 12:00"));
			Query query1 = cache.createQuery("TEST-BTY0:AI1", VType.class,
					new QueryParameters().timeInterval(TimeRelativeInterval.of(start, end)));
			// test that the cache is holding 1 PV cache
			Assert.assertEquals(1, ((CacheImpl) cache).getCount());

			QueryResult result = null;
			int updateCount = 0;
			int limit = 0;
			while (limit <= 60) { // 60s
				try {
					Thread.sleep(1000);
					result = query1.getUpdate();
					for (QueryData data : result.getData())
						updateCount += data.getCount();
					if (((QueryImpl) query1).isComplete())
						break;
				} catch (InterruptedException e) {
				}
				limit++;
			}
			int resultCount = 0;
			result = query1.getResult();
			for (QueryData data : result.getData())
				resultCount += data.getCount();
			Assert.assertEquals(updateCount, resultCount);

			Query query2 = cache.createQuery("TEST-BTY0:AI1",
					VType.class, new QueryParameters()
							.timeInterval(TimeRelativeInterval.of(start, end)));
			// test that the cache is still holding 1 PV cache
			Assert.assertEquals(1, ((CacheImpl) cache).getCount());

			// test that caches are the same
			PVCacheImpl pvCache1 = (PVCacheImpl) ((QueryImpl) query1).getCache();
			PVCacheImpl pvCache2 = (PVCacheImpl) ((QueryImpl) query2).getCache();
			Assert.assertEquals(pvCache1.getChannelName(), pvCache2.getChannelName());

			IntervalsList iList = pvCache1.getCompletedIntervalsList();
			Assert.assertTrue(iList.contains(TimeInterval.between(start, end)));

			int newUpdateCount = 0;
			limit = 0;
			while (limit <= 60) { // 60s
				try {
					Thread.sleep(1000);
					result = query2.getUpdate();
					for (QueryData data : result.getData())
						newUpdateCount += data.getCount();
					if (((QueryImpl) query2).isComplete())
						break;
				} catch (InterruptedException e) {
				}
				limit++;
			}
			int newResultCount = 0;
			result = query2.getResult();
			for (QueryData data : result.getData())
				newResultCount += data.getCount();

			Assert.assertEquals(updateCount, newUpdateCount);
			Assert.assertEquals(resultCount, newResultCount);

			iList = pvCache1.getCompletedIntervalsList();
			Assert.assertTrue(iList.contains(TimeInterval.between(start, end)));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}

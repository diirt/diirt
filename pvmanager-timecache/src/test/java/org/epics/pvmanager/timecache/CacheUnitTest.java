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
	 * channel name, the same {@link PVCache} is used and the Query.getResult
	 * samples count is the same.
	 */
	@Test
	public void testCreateQuery() {
		try {
			Cache cache = CacheFactory.getCache();
			start = Timestamp.of(dateFormat.parse("2014-04-03 09:00"));
			end = Timestamp.of(dateFormat.parse("2014-04-03 10:00"));
			Query query1 = cache.createQuery("TEST-BTY0:AI1",
					VType.class, new QueryParameters()
							.timeInterval(TimeRelativeInterval.of(start, end)));
			// test that the cache is holding 1 PV cache
			Assert.assertEquals(1, ((CacheImpl) cache).getCount());

			QueryResult result = null;
			int updateCount = 0;
			int limit = 0;
			while (limit <= 10) { // 10s
				try {
					Thread.sleep(1000);
					result = query1.getUpdate();
					for (QueryData data : result.getData())
						updateCount += data.getCount();
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

			PVCacheImpl pvCache1 = (PVCacheImpl) ((QueryImpl) query1).getCache();
			PVCacheImpl pvCache2 = (PVCacheImpl) ((QueryImpl) query2).getCache();
			// test that caches are the same
			Assert.assertEquals(pvCache1.getChannelName(), pvCache2.getChannelName());

			int newResultCount = 0;
			result = query1.getResult();
			for (QueryData data : result.getData())
				newResultCount += data.getCount();
			Assert.assertEquals(resultCount, newResultCount);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}

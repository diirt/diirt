/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import org.diirt.datasource.timecache.Data;
import org.diirt.datasource.timecache.DataChunk;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.impl.SimpleMemoryStorage;
import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link SimpleMemoryStorage}: stores provided samples.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class DataStorageUnitTest {

	/**
	 * Test that wrong parameters setting does not raise an exception. Test that
	 * no data is lost when writing/reading from storage. Test that exotic
	 * intervals are well handled.
	 */
	@Test
	public void testStorage() {
		DataSource source = new SimpleFileDataSource(
				"resources/test/archive-export.csv");
		SimpleMemoryStorage storage = new SimpleMemoryStorage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			// retrieve chunk
			Date startDate = sdf.parse("2014-03-14 16:00");
			DataChunk chunk = source.getData("TEST-BTY0:AI1", Timestamp.of(startDate));
			int chunkSize = chunk.getDatas().size();
			TimeInterval chunkInterval = chunk.getInterval();

			// test wrong parameters
			SortedSet<Data> dataSet = null;
			storage.storeData(null);
			dataSet = storage.getAvailableData(null);
			Assert.assertTrue(dataSet.isEmpty());
			dataSet = storage.getAvailableData(chunkInterval);
			Assert.assertTrue(dataSet.isEmpty());

			// store/retrieve data and compare
			storage.storeData(chunk);
			dataSet = storage.getAvailableData(chunkInterval);
			Assert.assertEquals(chunkSize, dataSet.size());
			Iterator<Data> itSet = dataSet.iterator();
			Iterator<Data> itChunk = chunk.getDatas().iterator();
			for (int index = 0; index < chunkSize; index++) {
				Assert.assertTrue(itChunk.hasNext());
				Assert.assertTrue(itSet.hasNext());
				Assert.assertEquals(itChunk.next().getTimestamp(), 
						itSet.next().getTimestamp());
			}

			// test intervals with one/both missing border
			dataSet = storage.getAvailableData(TimeInterval.between(chunkInterval.getStart(), null));
			Assert.assertEquals(chunkSize, dataSet.size());
			dataSet = storage.getAvailableData(TimeInterval.between(null, chunkInterval.getEnd()));
			Assert.assertEquals(chunkSize, dataSet.size());
			dataSet = storage.getAvailableData(TimeInterval.between(null, null));
			Assert.assertEquals(chunkSize, dataSet.size());

			// test interval with reversed begin/end
			dataSet = storage.getAvailableData(TimeInterval.between(
					chunkInterval.getEnd(), chunkInterval.getStart()));
			Assert.assertEquals(chunkSize, dataSet.size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}

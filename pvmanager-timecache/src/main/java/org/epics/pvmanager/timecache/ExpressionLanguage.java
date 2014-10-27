/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.epics.pvmanager.BasicTypeSupport;
import org.epics.pvmanager.ReadFunction;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.DesiredRateExpressionListImpl;
import org.epics.pvmanager.timecache.query.Query;
import org.epics.pvmanager.timecache.query.QueryData;
import org.epics.pvmanager.timecache.query.QueryParameters;
import org.epics.pvmanager.timecache.query.QueryResult;
import org.epics.pvmanager.timecache.util.CacheHelper;
import org.epics.pvmanager.vtype.DataTypeSupport;
import org.epics.util.array.ArrayDouble;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VDouble;
import org.epics.vtype.VTable;
import org.epics.vtype.VType;
import org.epics.vtype.ValueFactory;

/**
 * PVManager expression language support for additional operations.
 * 
 * @author carcassi
 */
public class ExpressionLanguage {

	private ExpressionLanguage() {
	}

	static {
		// Add support for Epics types.
		DataTypeSupport.install();
		// Add support for Basic types
		BasicTypeSupport.install();
	}

	/**
	 * A query expression that returns the result formatted as a table.
	 * 
	 * @param channelName
	 * @param parameters
	 * @return a new expression
	 */
	public static DesiredRateExpression<VTable> timeTableOf(
			final String channelName, QueryParameters parameters) {
		final Cache cache = CacheFactory.getCache();
		final Query query = cache.createQuery(channelName, VType.class, parameters);
		return new DesiredRateExpressionImpl<VTable>(new DesiredRateExpressionListImpl<Object>(),
				new ReadFunction<VTable>() {

					private NavigableMap<Timestamp, Double> valueMap;
					private VTable previousValue;

					{
						valueMap = new ConcurrentSkipListMap<Timestamp, Double>();
						previousValue = ValueFactory.newVTable(Arrays.<Class<?>> asList(String.class, double.class),
								Arrays.asList("Time", "Value"), 
								Arrays.asList(Arrays.asList(channelName), new ArrayDouble(0)));
					}

					@Override
					public VTable readValue() {
						// Create table by merging chunks as they come

						// Get new data
						QueryResult result = query.getUpdate();

						// If not new data is available, return the previous table
						if (result.getData().isEmpty())
							return previousValue;

						int index = 0;
						for (QueryData data : result.getData()) {
							for (VType dataToDisplay : data.getData()) {
								if (dataToDisplay instanceof VDouble)
									valueMap.put(data.getTimestamps().get(index), ((VDouble) dataToDisplay).getValue());
								index++;
							}
						}
						index = 0;
						double[] array = new double[valueMap.size()];
						List<String> times = new ArrayList<String>();
						for (Entry<Timestamp, Double> entry : valueMap.entrySet()) {
							times.add(CacheHelper.format(entry.getKey()));
							array[index] = entry.getValue();
							index++;
						}

						previousValue = ValueFactory.newVTable(Arrays.<Class<?>> asList(String.class, double.class),
								Arrays.asList("Time", "Value"), 
								Arrays.asList(times, new ArrayDouble(array, true)));
						return previousValue;
					}
				}, channelName);
	}

}

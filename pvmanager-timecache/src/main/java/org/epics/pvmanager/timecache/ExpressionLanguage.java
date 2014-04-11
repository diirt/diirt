/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.epics.pvmanager.vtype.DataTypeSupport;
import org.epics.pvmanager.BasicTypeSupport;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.ReadFunction;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.DesiredRateExpressionListImpl;
import org.epics.pvmanager.timecache.query.Query;
import org.epics.pvmanager.timecache.query.QueryData;
import org.epics.pvmanager.timecache.query.QueryParameters;
import org.epics.pvmanager.timecache.query.QueryResult;
import org.epics.pvmanager.timecache.util.CacheHelper;
import org.epics.util.array.ArrayDouble;
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

					private VTable previousValue;

					{
						previousValue = ValueFactory.newVTable(Arrays.<Class<?>> asList(String.class, double.class),
								Arrays.asList("Name", "Value"), 
								Arrays.asList(Arrays.asList(channelName, "B", "C"), new ArrayDouble(3.4, 1.2, 6.5)));
					}

					@Override
					public VTable readValue() {
						// Create table by merging chunks as they come

						// Get new data
						QueryResult result = query.getUpdate();

						// If not new data is available, return the previous table
						if (result.getData().isEmpty())
							return previousValue;

						// Count data
						int count = 0;
						for (QueryData data : result.getData())
							count += data.getCount();

						int index = 0;
						List<String> timestamps = new ArrayList<String>();
						double[] array = new double[count];
						for (QueryData data : result.getData()) {
							for (VType dataToDisplay : data.getData()) {
								if (dataToDisplay instanceof VDouble
										&& index < array.length) {
									array[index] = ((VDouble) dataToDisplay).getValue();
									timestamps.add(CacheHelper.format(data.getTimestamps().get(index)));
								}
								index++;
							}
						}

						previousValue = ValueFactory.newVTable(Arrays.<Class<?>> asList(String.class, double.class),
								Arrays.asList("Time", "Value"),
								Arrays.asList(timestamps, new ArrayDouble(array, true)));
						return previousValue;
					}
				}, channelName);
	}

}

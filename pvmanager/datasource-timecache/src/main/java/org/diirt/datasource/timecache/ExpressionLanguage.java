/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.diirt.datasource.BasicTypeSupport;
import org.diirt.datasource.ReadFunction;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import org.diirt.datasource.expression.DesiredRateExpressionListImpl;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.impl.SimpleMemoryStorage;
import org.diirt.datasource.timecache.query.Query;
import org.diirt.datasource.timecache.query.QueryData;
import org.diirt.datasource.timecache.query.QueryParameters;
import org.diirt.datasource.timecache.query.QueryResult;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.datasource.vtype.DataTypeSupport;
import org.diirt.util.array.ArrayDouble;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;

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
                // TODO: Cache & query should not be created here in order to be
                // configured / closed
                CacheConfig config = new CacheConfig();
                config.addSource(new SimpleFileDataSource("src/test/resources/archive-export.csv"));
                config.addSource(new SimpleFileDataSource("src/test/resources/archive-export-singlePV.csv"));
                config.setStorage(new SimpleMemoryStorage());
                final Cache cache = CacheFactory.getCache(config);
                final Query query = cache.createQuery(channelName, VType.class, parameters);
                return new DesiredRateExpressionImpl<VTable>(new DesiredRateExpressionListImpl<Object>(),
                                new ReadFunction<VTable>() {

                    private NavigableMap<Instant, Double> valueMap;
                    private VTable previousValue;

                    {
                        valueMap = new ConcurrentSkipListMap<Instant, Double>();
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
                        for (Entry<Instant, Double> entry : valueMap.entrySet()) {
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

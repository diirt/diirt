/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.integration;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import org.diirt.datasource.formula.StatefulFormulaFunction;
import org.diirt.datasource.timecache.Cache;
import org.diirt.datasource.timecache.CacheConfig;
import org.diirt.datasource.timecache.CacheFactory;
import org.diirt.datasource.timecache.impl.SimpleFileDataSource;
import org.diirt.datasource.timecache.impl.SimpleMemoryStorage;
import org.diirt.datasource.timecache.query.Query;
import org.diirt.datasource.timecache.query.QueryData;
import org.diirt.datasource.timecache.query.QueryParameters;
import org.diirt.datasource.timecache.query.QueryResult;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.time.TimeRelativeInterval;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class TCQueryFunction extends StatefulFormulaFunction {

    final private static DateTimeFormatter tsFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    final private static String S1 = "src/test/resources/archive-ramps-1H.csv"; // from '2014-12-04 00:00:01' to '2014-12-04 01:00:01'
    final private static String S2 = "src/test/resources/archive-ramps-1D.csv"; // from '2014-12-03 00:00:00' to '2014-12-04 00:00:00'
    final private static String S3 = "src/test/resources/archive-ramps-1W.csv"; // from '2014-11-25 23:59:59' to '2014-12-02 23:59:59'
    final private static String S4 = "src/test/resources/archive-ramps-2DUS.csv"; // from '2014-12-02 00:00:00' to '2014-12-04 00:00:00' but only multiple of 5

    private final Cache cache;
    private Query currentQuery;
    private String currentPV;
    private Instant currentBegin;
    private Instant currentEnd;

    private NavigableMap<Instant, Double> valueMap;
    private VTable previousValue;

    public TCQueryFunction() {
        valueMap = new ConcurrentSkipListMap<Instant, Double>();
        CacheConfig config = new CacheConfig();
        config.addSource(new SimpleFileDataSource(S4));
        config.addSource(new SimpleFileDataSource(S3));
        config.addSource(new SimpleFileDataSource(S2));
        config.addSource(new SimpleFileDataSource(S1));
        config.setStorage(new SimpleMemoryStorage());
        cache = CacheFactory.getCache(config);
        cache.setStatisticsEnabled(true);
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public String getName() {
        return "tcQuery";
    }

    @Override
    public String getDescription() {
        return "Query for data threw a TimeCache";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>> asList(VString.class, VString.class,
                VString.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("pvName", "begin", "end");
    }

    @Override
    public Class<?> getReturnType() {
        return VTable.class;
    }

    @Override
    public Object calculate(List<Object> args) {
        if (args == null || args.size() < 3) {
            return null;
        }
        if (previousValue == null) {
            previousValue = ValueFactory.newVTable(
                    Arrays.<Class<?>> asList(String.class, double.class),
                    Arrays.asList("Timestamp", "Value"),
                    Arrays.asList(new ArrayList<String>(), new ArrayDouble()));
        }
        String newPV = ((VString) args.get(0)).getValue();
        Instant newBegin = null, newEnd = null;
        try {
            newBegin = LocalDateTime.parse(((VString) args.get(1)).getValue(), tsFormat).toInstant(ZoneOffset.UTC);
            newEnd = LocalDateTime.parse(((VString) args.get(2)).getValue(), tsFormat).toInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException e) {
            return previousValue;
        }
        if (currentPV == null || !currentPV.equals(newPV)) {
            currentPV = newPV;
            currentBegin = newBegin;
            currentEnd = newEnd;
            QueryParameters parameters = new QueryParameters()
                    .timeInterval(TimeRelativeInterval.of(newBegin, newEnd));
            if (currentQuery != null) {
                currentQuery.close();
            }
            currentQuery = cache.createQuery(currentPV, VType.class, parameters);
        } else if (!currentBegin.equals(newBegin) || !currentEnd.equals(newEnd)) {
            QueryParameters parameters = new QueryParameters()
                    .timeInterval(TimeRelativeInterval.of(newBegin, newEnd));
            currentQuery.update(parameters);
        }
        // Create table by merging chunks as they come
        QueryResult result = currentQuery.getUpdate();

        // If not new data is available, return the previous table
        if (result.getData().isEmpty()) {
            return previousValue;
        }

        int index = 0;
        for (QueryData data : result.getData()) {
            index = 0;
            for (VType dataToDisplay : data.getData()) {
                if (dataToDisplay instanceof VDouble) {
                    valueMap.put(data.getTimestamps().get(index),
                            ((VDouble) dataToDisplay).getValue());
                }
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

        previousValue = ValueFactory.newVTable(
                Arrays.<Class<?>> asList(String.class, double.class),
                Arrays.asList("Timestamp", "Value"),
                Arrays.asList(times, new ArrayDouble(array, true)));
        return previousValue;
    }

    @Override
    public void dispose() {
        if (currentQuery != null) {
            currentQuery.close();
        }
    }

}

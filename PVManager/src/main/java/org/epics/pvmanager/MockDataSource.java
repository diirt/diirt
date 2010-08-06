/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import gov.aps.jca.dbr.DBR_TIME_Double;
import gov.aps.jca.dbr.Severity;
import gov.aps.jca.dbr.Status;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A data source for test data.
 *
 * @author carcassi
 */
public class MockDataSource extends DataSource {

    public static DataSource mockData() {
        return MockDataSource.instance;
    }

    private static Logger log = Logger.getLogger(DataSource.class.getName());
    static final MockDataSource instance = new MockDataSource();

    private static Random rand = new Random();

    private static Timer timer = new Timer("Mock Data Generator", true);

    private static void generateData(final Collector collector, final ValueCache<Double> value, final int nTimes, long period, final int samplesPerPeriod, final String type) {
        timer.scheduleAtFixedRate(new TimerTask() {
            int innerCounter;
            ValueProcessor<Object, Double> processor = new ValueProcessor<Object, Double>(collector, value) {

                @Override
                public void close() {
                    cancel();
                }

                @Override
                public boolean updateCache(Object payload, ValueCache<Double> cache) {
                    if ("linear".equals(type)) {
                        cache.setValue((double) innerCounter % 100);
                    } else {
                        cache.setValue(rand.nextGaussian());
                    }
                    return true;
                }
            };

            @Override
            public void run() {
                for (int i = 0; i < samplesPerPeriod; i++) {
                    processor.processValue(null);
                }
                innerCounter++;
                if (innerCounter == nTimes) {
                    log.fine("Stopped generating data on " + collector);
                    processor.close();
                }
            }
        }, 0, period);
        log.fine("Generating data on " + collector);
    }

    private static void generateDBRTimeDouble(final Collector collector, final ValueCache<DBR_TIME_Double> value,
            final int nTimes, long period, final int samplesPerPeriod, final String type) {
        timer.scheduleAtFixedRate(new TimerTask() {
            int innerCounter;
            ValueProcessor<Object, DBR_TIME_Double> processor = new ValueProcessor<Object, DBR_TIME_Double>(collector, value) {

                @Override
                public void close() {
                    cancel();
                }

                @Override
                public boolean updateCache(Object payload, ValueCache<DBR_TIME_Double> cache) {
                    DBR_TIME_Double newValue = new DBR_TIME_Double();
                    newValue.setSeverity(Severity.NO_ALARM);
                    newValue.setStatus(Status.NO_ALARM);
                    if ("linear".equals(type)) {
                        newValue.getDoubleValue()[0] = innerCounter % 100;
                    } else {
                        newValue.getDoubleValue()[0] = rand.nextGaussian();
                    }
                    TimeStamp now = TimeStamp.now();
                    newValue.setTimeStamp(new gov.aps.jca.dbr.TimeStamp(now.getEpicsSec(), now.getNanoSec()));
                    cache.setValue(newValue);
                    return true;
                }
            };

            @Override
            public void run() {
                for (int i = 0; i < samplesPerPeriod; i++) {
                    processor.processValue(null);
                }
                innerCounter++;
                if (innerCounter == nTimes) {
                    log.fine("Stopped generating data on " + collector);
                    processor.close();
                }
            }
        }, 0, period);
        log.fine("Generating data on " + collector);
    }

    Pattern pattern = Pattern.compile("(\\d*)samples_every(\\d*)ms_for(\\d*)times(.*)");

    private void connect(String name, Collector collector, ValueCache<Double> doubleCache) {
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            String type = matcher.group(4);
            int nTimes = Integer.parseInt(matcher.group(3));
            long period = Long.parseLong(matcher.group(2));
            int samplesPerPeriod = Integer.parseInt(matcher.group(1));
            generateData(collector, doubleCache, nTimes, period, samplesPerPeriod, type);
        } else {
            throw new RuntimeException("Name doesn't match for mock connection");
        }
    }

    private void connectDBR(String name, Collector collector, ValueCache<DBR_TIME_Double> cache) {
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            String type = matcher.group(4);
            int nTimes = Integer.parseInt(matcher.group(3));
            long period = Long.parseLong(matcher.group(2));
            int samplesPerPeriod = Integer.parseInt(matcher.group(1));
            generateDBRTimeDouble(collector, cache, nTimes, period, samplesPerPeriod, type);
        } else {
            throw new RuntimeException("Name doesn't match for mock connection");
        }
    }

    public static String mockPVName(int samplesPerNotification, long notificationPeriodMs, int nNotifications) {
        return "" + samplesPerNotification + "samples_every" + notificationPeriodMs + "ms_for" + nNotifications + "times";
    }

    @Override
    public void monitor(MonitorRecipe recipe) {
        for (Map.Entry<String, ValueCache> entry : recipe.caches.entrySet()) {
            createMonitor(recipe.collector, entry.getKey(), entry.getValue());
        }
    }

    public void createMonitor(Collector collector, String pvName, ValueCache<?> cache) {
        if (cache.getType().equals(Double.class)) {
            @SuppressWarnings("unchecked")
            ValueCache<Double> doubleCache = (ValueCache<Double>) cache;
            connect(pvName, collector, doubleCache);
        } else if (cache.getType().equals(DBR_TIME_Double.class)) {
            @SuppressWarnings("unchecked")
            ValueCache<DBR_TIME_Double> dbrTimeCache = (ValueCache<DBR_TIME_Double>) cache;
            connectDBR(pvName, collector, dbrTimeCache);
        } else {
            throw new UnsupportedOperationException("Type " + cache.getType().getName() + " is not yet supported");
        }
    }

}

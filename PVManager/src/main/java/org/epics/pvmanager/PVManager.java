/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.epics.pvmanager.util.ThreadFactories;
import org.epics.pvmanager.util.TimeDuration;

/**
 * Manages the PVReader creation and scanning.
 *
 * @author carcassi
 */
public class PVManager {

    static volatile Executor defaultNotificationExecutor = ThreadSwitch.onDefaultThread();
    private static volatile DataSource defaultDataSource = null;

    /**
     * Changes the default thread on which notifications are going to be posted.
     *
     * @param threadSwitch the new target thread
     */
    public static void setDefaultThread(Executor threadSwitch) {
        defaultNotificationExecutor = threadSwitch;
    }

    /**
     * Changes the default source for data.
     *
     * @param dataSource the data source
     */
    public static void setDefaultDataSource(DataSource dataSource) {
        PVManager.defaultDataSource = dataSource;
    }

    /**
     * The current data source.
     * 
     * @return a data source or null if it was not set
     */
    public static DataSource getDefaultDataSource() {
        return defaultDataSource;
    }

    /**
     * Reads the given expression. Will return the average of the values collected
     * at the scan rate.
     *
     * @param <T> type of the pv value
     * @param pvExpression the expression to read
     * @return a pv manager expression
     */
    public static <T> PVReaderConfiguration<T> read(SourceRateExpression<T> pvExpression) {
        return new PVReaderConfiguration<T>(ExpressionLanguage.latestValueOf(pvExpression));
    }

    /**
     * Reads the given expression.
     *
     * @param <T> type of the pv value
     * @param pvExpression the expression to read
     * @return a pv manager expression
     */
    public static <T> PVReaderConfiguration<T> read(DesiredRateExpression<T> pvExpression) {
        return new PVReaderConfiguration<T>(pvExpression);
    }
    
    public static <T> PVWriterConfiguration<T> write(WriteExpression<T> writeExpression) {
        return new PVWriterConfiguration<T>(writeExpression);
    }
    
    // TODO: this should be configurable, and probably split in asyncWriteExecutor and scannerExectuor
    static ScheduledExecutorService pvManagerThreadPool = Executors.newSingleThreadScheduledExecutor(ThreadFactories.namedPool("PVMgr Worker "));
    
    
}

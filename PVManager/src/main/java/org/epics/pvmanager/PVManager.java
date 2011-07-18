/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.epics.pvmanager.util.ThreadFactories;

/**
 * Entry point for the library, manages the defaults and allows to create
 * {@link PVReader}, {@link PVWriter} and {@link PV } from an read or write expression.
 * <p>
 * <b>AsynchWriteExecutor</b> - This is used for asynchronous writes, to return
 * right away. By default this uses the internal PVManager work pool. The work
 * submitted here is the calculation of the corresponding {@link WriteExpression}
 * and submission to the {@link DataSource}. The DataSource itself typically
 * has asynchronous work, which is executed in the DataSource specific threads.
 * Changing this to {@link ThreadSwitch#onLocalThread()} will make that preparation
 * task on the thread that calls {@link PVWriter#write(java.lang.Object) } but
 * it will not transform the call in a synchronous call.
 * <p>
 * <b>ReadScannerExecutorService</b> - This is used to run the periodic
 * scan for new values. By default this uses the internal PVManager work pool. The work
 * submitted here is the calculation of the corresponding {@link DesiredRateExpression}
 * and submission to the NotificationExecutor.
 *
 * @author carcassi
 */
public class PVManager {

    private static volatile Executor defaultNotificationExecutor = ThreadSwitch.onLocalThread();
    private static volatile DataSource defaultDataSource = null;
    private static final ScheduledExecutorService workerPool = Executors.newSingleThreadScheduledExecutor(ThreadFactories.namedPool("PVMgr Worker "));
    private static ScheduledExecutorService readScannerExecutorService = workerPool;
    private static Executor asyncWriteExecutor = workerPool;

    /**
     * Changes the default executor on which all notifications are going to be posted.
     *
     * @param threadSwitch the new target thread
     */
    public static void setDefaultNotificationExecutor(Executor threadSwitch) {
        defaultNotificationExecutor = threadSwitch;
    }

    /**
     * Returns the current default executor that will execute all notifications.
     * 
     * @return the default executor
     */
    public static Executor getDefaultNotificationExecutor() {
        return defaultNotificationExecutor;
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
     * Returns the current default data source.
     * 
     * @return a data source or null if it was not set
     */
    public static DataSource getDefaultDataSource() {
        return defaultDataSource;
    }

    /**
     * Reads the given expression, and returns an object to configure the parameters
     * for the read. At each notification it will return the latest value,
     * even if more had been received from the last notification.
     *
     * @param <T> type of the read payload
     * @param pvExpression the expression to read
     * @return the read configuration
     */
    public static <T> PVReaderConfiguration<T> read(SourceRateExpression<T> pvExpression) {
        return new PVReaderConfiguration<T>(ExpressionLanguage.latestValueOf(pvExpression));
    }

    /**
     * Reads the given expression, and returns an object to configure the parameters
     * for the read.
     *
     * @param <T> type of the read payload
     * @param pvExpression the expression to read
     * @return the read configuration
     */
    public static <T> PVReaderConfiguration<T> read(DesiredRateExpression<T> pvExpression) {
        return new PVReaderConfiguration<T>(pvExpression);
    }
    
    /**
     * Writes the given expression, and returns an object to configure the parameters
     * for the write.
     *
     * @param <T> type of the write payload
     * @param writeExpression the expression to write
     * @return the write configuration
     */
    public static <T> PVWriterConfiguration<T> write(WriteExpression<T> writeExpression) {
        return new PVWriterConfiguration<T>(writeExpression);
    }
    
    /**
     * Both reads and writes the given expression, and returns an object to configure the parameters
     * for the both read and write. It's similar to use both {@link #read(org.epics.pvmanager.SourceRateExpression) }
     * and {@link #write(org.epics.pvmanager.WriteExpression) ) at the same time.
     *
     * @param <R> type of the read payload
     * @param <W> type of the write payload
     * @param readWriteExpression the expression to read and write
     * @return the read and write configuration
     */
    public static <R, W> PVConfiguration<R, W> readAndWrite(ReadWriteExpression<R, W> readWriteExpression) {
        return new PVConfiguration<R, W>(ExpressionLanguage.latestValueOf(readWriteExpression.getSourceRateExpressionImpl()),
                readWriteExpression.getWriteExpressionImpl());
    }

    /**
     * Returns the current executor on which the asynchronous calls are executed.
     * 
     * @return the current executor
     */
    public static Executor getAsyncWriteExecutor() {
        return asyncWriteExecutor;
    }

    /**
     * Changes the executor used for the asynchronous write calls.
     * 
     * @param asyncWriteExecutor the new executor
     */
    public static void setAsyncWriteExecutor(Executor asyncWriteExecutor) {
        PVManager.asyncWriteExecutor = asyncWriteExecutor;
    }

    /**
     * Returns the executor service used to schedule and run the 
     * periodic reading scan for new values.
     * 
     * @return the service for the read operations
     */
    public static ScheduledExecutorService getReadScannerExecutorService() {
        return readScannerExecutorService;
    }

    /**
     * Changes the executor service to use for executing the periodic read
     * scan.
     * 
     * @param readScannerExecutorService  the new service for the read operations
     */
    public static void setReadScannerExecutorService(ScheduledExecutorService readScannerExecutorService) {
        PVManager.readScannerExecutorService = readScannerExecutorService;
    }
    
}

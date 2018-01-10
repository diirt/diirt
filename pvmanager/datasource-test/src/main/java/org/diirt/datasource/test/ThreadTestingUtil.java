/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;

import org.diirt.datasource.PVReader;
import org.diirt.util.time.TimeInterval;

/**
 *
 * @author carcassi
 */
public class ThreadTestingUtil {

    /**
     * Waits until either the task returns a value or the timeout expires.
     * The task is repeated every ms.
     *
     * @param <T> the return type of the task
     * @param task the task
     * @param timeout the timeout
     * @return the value from the task or null
     * @throws Exception an exception from the task
     */
    public static <T> T waitFor(Callable<T> task, Duration timeout)
    throws Exception {
        TimeInterval runInterval = TimeInterval.after(timeout, Instant.now());
        while (runInterval.contains(Instant.now())) {
            T value = task.call();
            if (value != null) {
                return value;
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    public static Duration waitForValue(PVReader<?> pvReader, Duration timeout)  {
        TimeInterval runInterval = TimeInterval.after(timeout, Instant.now());
        while (runInterval.contains(Instant.now())) {
            if (pvReader.getValue() != null) {
                return Duration.between(runInterval.getStart(), Instant.now());
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }


}

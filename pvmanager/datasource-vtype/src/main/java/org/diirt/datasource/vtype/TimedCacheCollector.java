/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.diirt.vtype.Time;
import java.util.*;
import org.diirt.datasource.Collector;
import org.diirt.datasource.ReadFunction;
import java.time.Duration;
import org.diirt.util.time.TimeInterval;

/**
 *
 * @author carcassi
 */
class TimedCacheCollector<T extends Time> implements Collector<T, List<T>> {

    private final Deque<T> buffer = new ArrayDeque<T>();
    private final ReadFunction<T> function;
    private final Duration cachedPeriod;
    private Runnable notification;

    public TimedCacheCollector(ReadFunction<T> function, Duration cachedPeriod) {
        this.function = function;
        this.cachedPeriod = cachedPeriod;
    }

    @Override
    public void setChangeNotification(Runnable notification) {
        synchronized (buffer) {
            this.notification = notification;
        }
    }

    @Override
    public void writeValue(T newValue) {
        Runnable task;
        // Buffer is locked and updated
        if (newValue != null) {
            synchronized(buffer) {
                buffer.add(newValue);
                prune();
                task = notification;
            }
            // Run the task without holding the lock
            if (task != null) {
                task.run();
            }
        }
    }

    /**
     * Returns all values since last check and removes values from the queue.
     * @return a new array with the value; never null
     */
    @Override
    public List<T> readValue() {
        synchronized(buffer) {
            if (buffer.isEmpty())
                return Collections.emptyList();

            return new ArrayList<T>(buffer);
        }
    }

    private void prune() {
        // Remove all values that are too old
        TimeInterval periodAllowed = TimeInterval.before(cachedPeriod, buffer.getLast().getTimestamp());
        while (!buffer.isEmpty() && !periodAllowed.contains(buffer.getFirst().getTimestamp())) {
            // Discard value
            buffer.removeFirst();
        }
    }

}

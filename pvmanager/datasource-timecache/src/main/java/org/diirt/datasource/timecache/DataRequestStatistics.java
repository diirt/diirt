/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.util.time.TimeInterval;

/**
 * Statistics of {@link DataRequestThread}
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class DataRequestStatistics {

    private final DataSource source;
    private final Instant start;
    private TimeInterval interval;
    private Duration duration;

    public DataRequestStatistics(final DataSource source) {
        this.start = Instant.now();
        this.source = source;
    }

    public void intervalCompleted() {
        this.duration = Duration.between(start, Instant.now()).abs();
    }

    public Instant getStart() {
        return start;
    }

    public Duration getDuration() {
        return duration;
    }

    public DataSource getSource() {
        return source;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public void setInterval(TimeInterval interval) {
        this.interval = interval;
    }

    public String toConsoleString() {
        return CacheHelper.format(start) + " => "
                + CacheHelper.format(interval) + ": " + duration;
    }

}

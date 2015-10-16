/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.util.CacheHelper;
import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;

/**
 * Statistics of {@link DataRequestThread}
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class DataRequestStatistics {

        private final DataSource source;
        private final Timestamp start;
        private TimeInterval interval;
        private TimeDuration duration;

        public DataRequestStatistics(final DataSource source) {
                this.start = Timestamp.now();
                this.source = source;
        }

        public void intervalCompleted() {
                this.duration = Timestamp.now().durationBetween(start);
        }

        public Timestamp getStart() {
                return start;
        }

        public TimeDuration getDuration() {
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

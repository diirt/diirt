/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.util;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;

/**
 * Transforms a set of {@link Timestamp} to an {@link IntervalsList}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class TimestampsSet {

        private Set<Timestamp> timestamps;
        private TimeDuration tolerance = TimeDuration.ofSeconds(1);

        /** Build an empty {@link TimestampsSet}. */
        public TimestampsSet() {
                timestamps = new TreeSet<Timestamp>();
        }

        /** Check if the instance is empty. */
        public boolean isEmpty() {
                return timestamps.isEmpty();
        }

        /** Get the number of {@link Timestamp} in the set. */
        public int getSize() {
                return timestamps.size();
        }

        /**
         * Add a {@link Timestamp} to the set.
         * @param t {@link Timestamp} to be added.
         */
        public void add(Timestamp t) {
                if (t != null)
                        timestamps.add(t);
        }

        /**
         * Set the {@link TimeDuration} used to define the minimum duration of
         * generated {@link TimeInterval}.
         * @param tolerance {@link TimeDuration} minimal duration
         */
        public void setTolerance(TimeDuration tolerance) {
                this.tolerance = tolerance;
        }

        /**
         * Returns an {@link IntervalsList} built from a set of {@link Timestamp}
         * using the following algorithm: if the {@link TimeDuration} between two
         * {@link Timestamp} is inferior to the defined tolerance, they are grouped
         * in the same {@link TimeInterval}. A {@link TimeInterval} duration can not
         * be inferior to the defined tolerance. TODO: better algorithm ?
         */
        public IntervalsList toIntervalsList() {
                IntervalsList ilist = new IntervalsList();
                if (isEmpty()) // empty list
                        return ilist;
                Iterator<Timestamp> iterator = timestamps.iterator();
                Timestamp first = iterator.next();
                Timestamp previous = first;
                while (iterator.hasNext()) {
                        Timestamp next = iterator.next();
                        if (previous.plus(tolerance).compareTo(next) < 0) {
                                if (first.equals(previous)) {
                                        ilist.addToSelf(intervalOfSinglePoint(first));
                                } else {
                                        ilist.addToSelf(TimeInterval.between(first, previous));
                                }
                                first = next;
                        }
                        previous = next;
                }
                // Process end of set
                if (first.equals(previous)) {
                        ilist.addToSelf(intervalOfSinglePoint(first));
                } else {
                        ilist.addToSelf(TimeInterval.between(first, previous));
                }
                return ilist;
        }

        // Generate a TimeInterval from a single timestamp
        private TimeInterval intervalOfSinglePoint(Timestamp first) {
                return TimeInterval.between(first, first.plus(tolerance));
        }

}

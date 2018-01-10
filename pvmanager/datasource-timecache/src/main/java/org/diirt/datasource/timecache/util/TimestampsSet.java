/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.diirt.util.time.TimeInterval;

/**
 * Transforms a set of {@link Timestamp} to an {@link IntervalsList}.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class TimestampsSet {

    private Set<Instant> timestamps;
    private Duration tolerance = Duration.ofSeconds(1);

    /** Build an empty {@link TimestampsSet}. */
    public TimestampsSet() {
        timestamps = new TreeSet<Instant>();
    }

    /** Check if the instance is empty. */
    public boolean isEmpty() {
        return timestamps.isEmpty();
    }

    /** Get the number of {@link Instant} in the set. */
    public int getSize() {
        return timestamps.size();
    }

    /**
     * Add a {@link Instant} to the set.
     * @param t {@link Instant} to be added.
     */
    public void add(Instant t) {
        if (t != null)
            timestamps.add(t);
    }

    /**
     * Set the {@link Duration} used to define the minimum duration of
     * generated {@link TimeInterval}.
     * @param tolerance {@link Duration} minimal duration
     */
    public void setTolerance(Duration tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Returns an {@link IntervalsList} built from a set of {@link Instant}
     * using the following algorithm: if the {@link Duration} between two
     * {@link Instant} is inferior to the defined tolerance, they are grouped
     * in the same {@link TimeInterval}. A {@link TimeInterval} duration can not
     * be inferior to the defined tolerance. TODO: better algorithm ?
     */
    public IntervalsList toIntervalsList() {
        IntervalsList ilist = new IntervalsList();
        if (isEmpty()) // empty list
            return ilist;
        Iterator<Instant> iterator = timestamps.iterator();
        Instant first = iterator.next();
        Instant previous = first;
        while (iterator.hasNext()) {
            Instant next = iterator.next();
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
    private TimeInterval intervalOfSinglePoint(Instant first) {
        return TimeInterval.between(first, first.plus(tolerance));
    }

}

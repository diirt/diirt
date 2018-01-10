/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.diirt.util.time.TimeInterval;

/**
 * This class represents an intervals list. <p> An interval list represent a
 * list of contiguous regions on the real line. All intervals of the list are
 * disjoints to each other, they are stored in ascending order. </p> <p> The
 * class supports the main set operations like union and intersection. </p>
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class IntervalsList {

    public static final Duration minDuration = Duration.ofNanos(1);

    /** The list of intervals. */
    private List<TimeInterval> intervals;

    /**
     * Build an empty intervals list.
     */
    public IntervalsList() {
        intervals = new ArrayList<TimeInterval>();
    }

    /**
     * Build an intervals list containing only one interval.
     * @param i interval
     */
    public IntervalsList(TimeInterval i) {
        intervals = new ArrayList<TimeInterval>();
        i = CacheHelper.arrange(i);
        intervals.add(i);
    }

    /**
     * Build an intervals list containing two intervals.
     * @param i1 first interval
     * @param i2 second interval
     */
    public IntervalsList(TimeInterval i1, TimeInterval i2) {
        intervals = new ArrayList<TimeInterval>();
        i1 = CacheHelper.arrange(i1);
        i2 = CacheHelper.arrange(i2);
        if (CacheHelper.intersects(i1, i2)) {
            intervals.add(TimeInterval.between(
                    CacheHelper.min(i1.getStart(), i2.getStart()),
                    CacheHelper.max(i1.getEnd(), i2.getEnd())));
        } else if (i1.getEnd().compareTo(i2.getStart()) < 0) {
            intervals.add(i1);
            intervals.add(i2);
        } else {
            intervals.add(i2);
            intervals.add(i1);
        }
    }

    /**
     * Copy constructor. <p> The copy operation is a deep copy: the underlying
     * intervals are independant of the instances of the copied list. </p>
     * @param list intervals list to copy
     */
    public IntervalsList(IntervalsList list) {
        intervals = new ArrayList<TimeInterval>(list.intervals.size());
        for (Iterator<TimeInterval> iterator = list.intervals.iterator(); iterator
                .hasNext();) {
            TimeInterval ti = (TimeInterval) iterator.next();
            intervals.add(TimeInterval.between(ti.getStart(), ti.getEnd()));
        }
    }

    /**
     * Check if the instance is empty.
     * @return true if the instance is empty
     */
    public boolean isEmpty() {
        return intervals.isEmpty();
    }

    /**
     * Check if the instance is connected. <p> An interval list is connected if
     * it contains only one interval. </p>
     * @return true is the instance is connected
     */
    public boolean isConnex() {
        return intervals.size() == 1;
    }

    /**
     * Get the lower bound of the list.
     * @return lower bound of the list or null if the list does not contain any
     *         interval
     */
    public Instant getStart() {
        return intervals.isEmpty() ? null : intervals.get(0).getStart();
    }

    /**
     * Get the upper bound of the list.
     * @return upper bound of the list or null if the list does not contain any
     *         interval
     */
    public Instant getEnd() {
        return intervals.isEmpty() ? null : intervals.get(intervals.size() - 1)
                .getEnd();
    }

    /**
     * Get the number of intervals of the list.
     * @return number of intervals in the list
     */
    public int getSize() {
        return intervals.size();
    }

    /**
     * Get an interval from the list.
     * @param i index of the interval
     * @return interval at index i
     */
    public TimeInterval getTimeInterval(int i) {
        return intervals.get(i);
    }

    /**
     * Get the ordered list of disjoints intervals.
     * @return list of disjoints intervals in ascending order
     */
    public List<TimeInterval> getIntervals() {
        return intervals;
    }

    /**
     * Check if the list contains a point.
     * @param t point to check
     * @return true if the list contains t
     */
    public boolean contains(Instant t) {
        if (t == null)
            return false;
        for (Iterator<TimeInterval> iterator = intervals.iterator(); iterator
                .hasNext();)
            if (iterator.next().contains(t))
                return true;
        return false;
    }

    /**
     * Check if the list contains an interval.
     * @param i interval to check
     * @return true if i is completely included in the instance
     */
    public boolean contains(TimeInterval i) {
        if (i == null)
            return false;
        for (Iterator<TimeInterval> iterator = intervals.iterator(); iterator
                .hasNext();)
            if (CacheHelper.contains(iterator.next(), i))
                return true;
        return false;
    }

    /**
     * Check if an interval intersects the instance.
     * @param i interval to check
     * @return true if i intersects the instance
     */
    public boolean intersects(TimeInterval i) {
        if (i == null)
            return false;
        for (Iterator<TimeInterval> iterator = intervals.iterator(); iterator
                .hasNext();)
            if (CacheHelper.intersects(iterator.next(), i))
                return true;
        return false;
    }

    /**
     * Add an interval to the instance. <p> This method expands the instance.
     * </p> <p> This operation is a union operation. The number of intervals in
     * the list can decrease if the interval fills some holes between existing
     * intervals in the list. </p>
     * @param i interval to add to the instance
     */
    public void addToSelf(TimeInterval i) {
        if (i == null)
            return;
        if (isConnex() && getStart() == null && getEnd() == null)
            return;
        if (i.getStart() == null || i.getEnd() == null) {
            handleAddNull(i);
            return;
        }
        i = CacheHelper.arrange(i);
        List<TimeInterval> newIntervals = new ArrayList<TimeInterval>();
        Instant inf = null;
        Instant sup = null;
        boolean pending = false;
        boolean processed = false;
        for (Iterator<TimeInterval> iterator = intervals.iterator(); iterator
                .hasNext();) {
            TimeInterval local = iterator.next();
            if (local.getEnd() != null
                    && local.getEnd().compareTo(i.getStart()) < 0) {
                newIntervals.add(local);
            } else if (local.getStart() == null
                    || local.getStart().compareTo(i.getEnd()) <= 0) {
                if (!pending) {
                    inf = CacheHelper.min(local.getStart(), i.getStart());
                    pending = true;
                    processed = true;
                }
                sup = CacheHelper.max(local.getEnd(), i.getEnd());
            } else {
                if (pending) {
                    newIntervals.add(TimeInterval.between(inf, sup));
                    pending = false;
                } else if (!processed) {
                    newIntervals.add(i);
                }
                processed = true;
                newIntervals.add(local);
            }
        }
        if (pending) {
            newIntervals.add(TimeInterval.between(inf, sup));
        } else if (!processed) {
            newIntervals.add(i);
        }
        intervals = mergeAllLimits(newIntervals);
    }

    private void handleAddNull(TimeInterval i) {
        List<TimeInterval> newIntervals = new ArrayList<TimeInterval>();
        if (i.getStart() == null && i.getEnd() == null) {
            newIntervals.add(TimeInterval.between(null, null));
        } else if (i.getStart() == null && i.getEnd() != null) {
            Instant sup = i.getEnd();
            for (Iterator<TimeInterval> iterator = intervals.iterator(); iterator
                    .hasNext();) {
                TimeInterval local = iterator.next();
                if (local.getStart() == null
                        || local.getStart().compareTo(i.getEnd()) <= 0)
                    sup = CacheHelper.max(local.getEnd(), sup);
                else newIntervals.add(local);
            }
            newIntervals.add(TimeInterval.between(null, sup));
        } else if (i.getStart() != null && i.getEnd() == null) {
            Instant inf = i.getStart();
            for (Iterator<TimeInterval> iterator = intervals.iterator(); iterator
                    .hasNext();) {
                TimeInterval local = iterator.next();
                if (local.getEnd() == null
                        || local.getEnd().compareTo(i.getStart()) >= 0)
                    inf = CacheHelper.min(local.getStart(), inf);
                else newIntervals.add(local);
            }
            newIntervals.add(TimeInterval.between(inf, null));
        }
        intervals = newIntervals;
    }

    private List<TimeInterval> mergeAllLimits(List<TimeInterval> intervals) {
        List<TimeInterval> newIntervals = new ArrayList<TimeInterval>();
        Iterator<TimeInterval> iterator = intervals.iterator();
        TimeInterval previous = null;
        TimeInterval current = null;
        if (iterator.hasNext())
            current = iterator.next();
        while (iterator.hasNext()) {
            previous = current;
            current = iterator.next();
            if (previous.getEnd().plus(minDuration).equals(current.getStart())) {
                TimeInterval merged = TimeInterval.between(previous.getStart(), current.getEnd());
                current = merged;
            } else {
                newIntervals.add(previous);
            }
        }
        if (current != null)
            newIntervals.add(current);
        return newIntervals;
    }

    /**
     * Add an intervals list and an interval.
     * @param list intervals list
     * @param i interval
     * @return a new intervals list which is the union of list and i
     */
    public static IntervalsList add(IntervalsList list, TimeInterval i) {
        IntervalsList copy = new IntervalsList(list);
        copy.addToSelf(i);
        return copy;
    }

    /**
     * Remove an interval from the list. <p> This method reduces the instance.
     * This operation is defined in terms of points set operation. As an
     * example, if the [2, 3] interval is subtracted from the list containing
     * only the [0, 10] interval, the result will be the [0, 2] U [3, 10]
     * intervals list. </p>
     * @param i interval to remove
     */
    public void subtractFromSelf(TimeInterval i) {
        if (i == null)
            return;
        if (i.getStart() == null && i.getEnd() == null) {
            intervals.clear();
            return;
        }
        IntervalsList inversedIntervals = new IntervalsList();
        if (i.getStart() == null && i.getEnd() != null) {
            inversedIntervals.addToSelf(TimeInterval.between(i.getEnd().plus(minDuration), null));
        } else if (i.getStart() != null && i.getEnd() == null) {
            inversedIntervals.addToSelf(TimeInterval.between(null, i.getStart().minus(minDuration)));
        } else {
            i = CacheHelper.arrange(i);
            inversedIntervals.addToSelf(TimeInterval.between(null, i.getStart().minus(minDuration)));
            inversedIntervals.addToSelf(TimeInterval.between(i.getEnd().plus(minDuration), null));
        }
        intersectSelf(inversedIntervals);
    }

    /**
     * Remove an interval from a list.
     * @param list intervals list
     * @param i interval to remove
     * @return a new intervals list
     */
    public static IntervalsList subtract(IntervalsList list, TimeInterval i) {
        IntervalsList copy = new IntervalsList(list);
        copy.subtractFromSelf(i);
        return copy;
    }

    /**
     * Intersects the instance and an interval.
     * @param i interval
     */
    public void intersectSelf(TimeInterval i) {
        List<TimeInterval> newIntervals = new ArrayList<TimeInterval>();
        for (Iterator<TimeInterval> iterator = intervals.iterator(); iterator
                .hasNext();) {
            TimeInterval local = iterator.next();
            if (CacheHelper.intersects(local, i)) {
                newIntervals.add(CacheHelper.intersection(local, i));
            }
        }
        intervals = newIntervals;
    }

    /**
     * Intersect a list and an interval.
     * @param list intervals list
     * @param i interval
     * @return the intersection of list and i
     */
    public static IntervalsList intersection(IntervalsList list, TimeInterval i) {
        IntervalsList copy = new IntervalsList(list);
        copy.intersectSelf(i);
        return copy;
    }

    /**
     * Add an intervals list to the instance. <p> This method expands the
     * instance. </p> <p> This operation is a union operation. The number of
     * intervals in the list can decrease if the list fills some holes between
     * existing intervals in the instance. </p>
     * @param list intervals list to add to the instance
     */
    public void addToSelf(IntervalsList list) {
        if (list == null)
            return;
        for (Iterator<TimeInterval> iterator = list.intervals.iterator(); iterator
                .hasNext();) {
            addToSelf(iterator.next());
        }
    }

    /**
     * Add two intervals lists.
     * @param list1 first intervals list
     * @param list2 second intervals list
     * @return a new intervals list which is the union of list1 and list2
     */
    public static IntervalsList add(IntervalsList list1, IntervalsList list2) {
        IntervalsList copy = new IntervalsList(list1);
        copy.addToSelf(list2);
        return copy;
    }

    /**
     * Remove an intervals list from the instance.
     * @param list intervals list to remove
     */
    public void subtractFromSelf(IntervalsList list) {
        if (list == null)
            return;
        for (Iterator<TimeInterval> iterator = list.intervals.iterator(); iterator
                .hasNext();) {
            subtractFromSelf(iterator.next());
        }
    }

    /**
     * Remove an intervals list from another one.
     * @param list1 intervals list
     * @param list2 intervals list to remove
     * @return a new intervals list
     */
    public static IntervalsList subtract(IntervalsList list1,
            IntervalsList list2) {
        IntervalsList copy = new IntervalsList(list1);
        copy.subtractFromSelf(list2);
        return copy;
    }

    /**
     * Intersect the instance and another intervals list.
     * @param list list to intersect with the instance
     */
    public void intersectSelf(IntervalsList list) {
        intervals = intersection(this, list).intervals;
    }

    /**
     * Intersect two intervals lists.
     * @param list1 first intervals list
     * @param list2 second intervals list
     * @return a new list which is the intersection of list1 and list2
     */
    public static IntervalsList intersection(IntervalsList list1,
            IntervalsList list2) {
        IntervalsList list = new IntervalsList();
        for (Iterator<TimeInterval> iterator = list2.intervals.iterator(); iterator
                .hasNext();) {
            list.addToSelf(intersection(list1, iterator.next()));
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("IntervalsList [intervals=");
        for (TimeInterval ti : intervals) {
            sb.append(CacheHelper.format(ti));
            sb.append(", ");
        }
        if (intervals.size() > 0)
            sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

}
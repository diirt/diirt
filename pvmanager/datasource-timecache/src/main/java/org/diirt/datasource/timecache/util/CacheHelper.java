/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;
import org.diirt.util.time.TimestampFormat;

/**
 * Cache helper.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheHelper {

        private static TimestampFormat tsFormat = new TimestampFormat("yyyy-MM-dd'T'HH:mm:ss.NNNNNNNNNZ");
        public static PrintStream ps = System.out;

        static {
                try {
                        ps = new PrintStream("src/test/resources/logs.txt");
                } catch (FileNotFoundException e) {
                }
        }

        /** Returns the minimum between the two specified {@link Timestamp}. */
        public static Timestamp min(Timestamp t1, Timestamp t2) {
                if (t1 == null || t2 == null) return null;
                if (t1.compareTo(t2) <= 0) return t1;
                else return t2;
        }

        /** Returns the maximum between the two specified {@link Timestamp}. */
        public static Timestamp max(Timestamp t1, Timestamp t2) {
                if (t1 == null || t2 == null) return null;
                if (t1.compareTo(t2) >= 0) return t1;
                else return t2;
        }

        /**
         * Returns <code>true</code> if {@link TimeInterval} i1 contains
         * {@link TimeInterval} i2, <code>false</code> otherwise (<code>null</code>
         * border means infinity).
         */
        public static boolean contains(TimeInterval i1, TimeInterval i2) {
                if (i1 == null || i2 == null)
                        return false;
                i1 = arrange(i1);
                i2 = arrange(i2);
                if (i2.getStart() == null && i2.getEnd() == null) return i1.getStart() == null && i1.getEnd() == null;
                else if (i2.getStart() != null && i2.getEnd() == null) return i1.contains(i2.getStart()) && i1.getEnd() == null;
                else if (i2.getStart() == null && i2.getEnd() != null) return i1.getStart() == null && i1.contains(i2.getEnd());
                else return i1.contains(i2.getStart()) && i1.contains(i2.getEnd());
        }

        /**
         * Returns <code>true</code> if {@link TimeInterval} i1 intersects
         * {@link TimeInterval} i2, <code>false</code> otherwise (<code>null</code>
         * border means infinity).
         */
        public static boolean intersects(TimeInterval i1, TimeInterval i2) {
                if (i1 == null || i2 == null)
                        return false;
                i1 = arrange(i1);
                i2 = arrange(i2);
                if (i1.getStart() == null && i1.getEnd() == null) { return true;
                } else if (i1.getStart() != null && i1.getEnd() == null) {
                        if (i2.getEnd() == null) return true;
                        else if (i2.getStart() == null && i2.getEnd() != null) return i1.contains(i2.getEnd());
                        else return i1.contains(i2.getEnd()) || i1.contains(i2.getStart());
                } else if (i1.getStart() == null && i1.getEnd() != null) {
                        if (i2.getStart() == null) return true;
                        else if (i2.getStart() != null && i2.getEnd() == null) return i1.contains(i2.getStart());
                        else return i1.contains(i2.getEnd()) || i1.contains(i2.getStart());
                } else {
                        if (i2.getStart() == null && i2.getEnd() == null) return true;
                        else if (i2.getStart() == null || i2.getEnd() == null) return i2.contains(i1.getEnd()) || i2.contains(i1.getStart());
                        else return i1.contains(i2.getStart()) || i1.contains(i2.getEnd())
                                                || i2.contains(i1.getStart()) || i2.contains(i1.getEnd());
                }
        }

        /**
         * Returns the result of {@link TimeInterval} i1 &amp; i2 intersection,
         * <code>null</code> if i1 does not intersects i2 (<code>null</code> border
         * means infinity).
         */
        public static TimeInterval intersection(TimeInterval i1, TimeInterval i2) {
                if (i1 == null || i2 == null)
                        return null;
                if (!intersects(i1, i2))
                        return null;
                i1 = arrange(i1);
                i2 = arrange(i2);
                Timestamp a = null;
                if (i1.getStart() == null && i2.getStart() == null) a = null;
                else if (i1.getStart() == null && i2.getStart() != null) a = i2.getStart();
                else if (i1.getStart() != null && i2.getStart() == null) a = i1.getStart();
                else a = max(i1.getStart(), i2.getStart());

                Timestamp b = null;
                if (i1.getEnd() == null && i2.getEnd() == null) b = null;
                else if (i1.getEnd() == null && i2.getEnd() != null) b = i2.getEnd();
                else if (i1.getEnd() != null && i2.getEnd() == null) b = i1.getEnd();
                else b = min(i1.getEnd(), i2.getEnd());

                return TimeInterval.between(a, b);
        }

        /** Returns a re-arranged {@link TimeInterval} if borders order is reversed. */
        public static TimeInterval arrange(TimeInterval i) {
                if (i.getStart() != null && i.getEnd() != null
                                && i.getStart().compareTo(i.getEnd()) > 0)
                        return TimeInterval.between(i.getEnd(), i.getStart());
                return i;
        }

        /** Returns a formated {@link String} from the specified {@link Timestamp}. */
        public static String format(Timestamp t) {
                return tsFormat.format(t);
        }

        /** Returns a formated {@link String} from the specified {@link TimeInterval}. */
        public static String format(TimeInterval i) {
                if (i == null) return "[]";
                return "[" + format(i.getStart()) + " - " + format(i.getEnd()) + "]";
        }

        /** Returns a formated {@link String} from the specified {@link TimeDuration}. */
        public static String format(TimeDuration d) {
                double seconds = d.toSeconds();
                long h = Math.round(seconds / 3600);
                long m = Math.round((seconds % 3600) / 60);
                long s = Math.round(seconds % 60);
                return String.format("%d:%02d:%02d", h, m, s);
        }

}

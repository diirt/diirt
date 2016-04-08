/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.util;

import java.text.ParseException;
import java.time.Instant;

import org.diirt.util.time.TimeInterval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link CacheHelper}. min: calculate min between 2 {@link Timestamp}.
 * max: calculate max between 2 {@link Timestamp}. contains: test if first
 * {@link TimeInterval} contains the other. intersects: test if first
 * {@link TimeInterval} intersects the other. intersection: return the
 * {@link TimeInterval} corresponding to the intersection of provided ones.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheHelperUnitTest {

    @Test
    public void testMin() {
        try {
            Instant t1 = UnitTestUtils.timestampOf("16:00");
            Instant t2 = UnitTestUtils.timestampOf("17:00");

            // test null parameters
            Assert.assertNull(CacheHelper.min(t1, null));
            Assert.assertNull(CacheHelper.min(null, t2));
            Assert.assertNull(CacheHelper.min(null, null));

            Assert.assertEquals(t1, CacheHelper.min(t1, t2));
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testMax() {
        try {
            Instant t1 = UnitTestUtils.timestampOf("16:00");
            Instant t2 = UnitTestUtils.timestampOf("17:00");

            // test null parameters
            Assert.assertNull(CacheHelper.max(t1, null));
            Assert.assertNull(CacheHelper.max(null, t2));
            Assert.assertNull(CacheHelper.max(null, null));

            Assert.assertEquals(t2, CacheHelper.max(t1, t2));
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that {@link TimeInterval} i1 contains i2. Test that exotic intervals
     * are well handled (null border means infinity).
     */
    @Test
    public void testContains() {
        try {
            TimeInterval i1 = UnitTestUtils.timeIntervalOf("16:00", "17:00");
            TimeInterval i2 = UnitTestUtils.timeIntervalOf("16:15", "16:45");

            // test null parameters
            Assert.assertFalse(CacheHelper.contains(i1, null));
            Assert.assertFalse(CacheHelper.contains(null, i2));
            Assert.assertFalse(CacheHelper.contains(null, null));

            Assert.assertTrue(CacheHelper.contains(i1, i2));

            // test reversed interval
            i1 = UnitTestUtils.timeIntervalOf("17:00", "16:00");
            i2 = UnitTestUtils.timeIntervalOf("16:15", "16:45");
            Assert.assertTrue(CacheHelper.contains(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", "16:15");
            i2 = UnitTestUtils.timeIntervalOf("16:10", "17:00");
            Assert.assertFalse(CacheHelper.contains(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf("16:10", "17:00");
            Assert.assertTrue(CacheHelper.contains(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf("16:10", null);
            Assert.assertTrue(CacheHelper.contains(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf(null, "17:00");
            i2 = UnitTestUtils.timeIntervalOf(null, "16:10");
            Assert.assertTrue(CacheHelper.contains(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf(null, "17:00");
            Assert.assertFalse(CacheHelper.contains(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf(null, null);
            i2 = UnitTestUtils.timeIntervalOf("16:00", "17:00");
            Assert.assertTrue(CacheHelper.contains(i1, i2));
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that {@link TimeInterval} i1 intersects i2. Test that exotic intervals
     * are well handled (null border means infinity).
     */
    @Test
    public void testIntersects() {
        try {
            TimeInterval i1 = UnitTestUtils.timeIntervalOf("16:00", "16:45");
            TimeInterval i2 = UnitTestUtils.timeIntervalOf("16:15", "17:00");

            // test null parameters
            Assert.assertFalse(CacheHelper.intersects(i1, null));
            Assert.assertFalse(CacheHelper.intersects(null, i2));
            Assert.assertFalse(CacheHelper.intersects(null, null));

            Assert.assertTrue(CacheHelper.intersects(i1, i2));

            // test reversed interval
            i1 = UnitTestUtils.timeIntervalOf("16:00", "16:45");
            i2 = UnitTestUtils.timeIntervalOf("17:00", "16:15");
            Assert.assertTrue(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", "16:15");
            i2 = UnitTestUtils.timeIntervalOf("16:15", "16:45");
            Assert.assertTrue(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", "16:15");
            i2 = UnitTestUtils.timeIntervalOf("16:45", "17:00");
            Assert.assertFalse(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf("15:00", "17:00");
            Assert.assertTrue(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf("16:10", "17:00");
            Assert.assertTrue(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf("15:00", null);
            Assert.assertTrue(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf(null, "17:00");
            i2 = UnitTestUtils.timeIntervalOf(null, "16:10");
            Assert.assertTrue(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf(null, "17:00");
            Assert.assertTrue(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf(null, "15:00");
            Assert.assertFalse(CacheHelper.intersects(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf(null, null);
            i2 = UnitTestUtils.timeIntervalOf("16:00", "17:00");
            Assert.assertTrue(CacheHelper.intersects(i1, i2));
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that {@link TimeInterval} r is the result of i1 & i2 intersection.
     * Test that exotic intervals are well handled (null border means infinity).
     */
    @Test
    public void testIntersection() {
        try {
            TimeInterval i1 = UnitTestUtils.timeIntervalOf("16:00", "16:45");
            TimeInterval i2 = UnitTestUtils.timeIntervalOf("16:15", "17:00");
            TimeInterval r = UnitTestUtils.timeIntervalOf("16:15", "16:45");

            // test null parameters
            Assert.assertNull(CacheHelper.intersection(null, null));
            Assert.assertNull(CacheHelper.intersection(i1, null));
            Assert.assertNull(CacheHelper.intersection(null, i2));

            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            // test reversed interval
            i1 = UnitTestUtils.timeIntervalOf("16:00", "16:45");
            i2 = UnitTestUtils.timeIntervalOf("17:00", "16:15");
            r = UnitTestUtils.timeIntervalOf("16:15", "16:45");
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", "16:15");
            i2 = UnitTestUtils.timeIntervalOf("16:15", "16:45");
            r = UnitTestUtils.timeIntervalOf("16:15", "16:15");
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf("15:00", "17:00");
            r = UnitTestUtils.timeIntervalOf("16:00", "17:00");
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf("16:10", "16:20");
            r = UnitTestUtils.timeIntervalOf("16:10", "16:20");
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf("15:00", null);
            r = UnitTestUtils.timeIntervalOf("16:00", null);
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf(null, "17:00");
            i2 = UnitTestUtils.timeIntervalOf(null, "16:10");
            r = UnitTestUtils.timeIntervalOf(null, "16:10");
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf(null, "17:00");
            r = UnitTestUtils.timeIntervalOf("16:00", "17:00");
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf("16:00", null);
            i2 = UnitTestUtils.timeIntervalOf(null, "15:00");
            r = null;
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf(null, null);
            i2 = UnitTestUtils.timeIntervalOf("16:00", "17:00");
            r = UnitTestUtils.timeIntervalOf("16:00", "17:00");
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));

            i1 = UnitTestUtils.timeIntervalOf(null, null);
            i2 = UnitTestUtils.timeIntervalOf(null, "17:00");
            r = UnitTestUtils.timeIntervalOf(null, "17:00");
            Assert.assertEquals(r, CacheHelper.intersection(i1, i2));
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

}

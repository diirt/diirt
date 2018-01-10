/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.util;

import java.text.ParseException;
import java.time.Instant;

import org.diirt.util.time.TimeInterval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link IntervalsList}: manages a list of disjoints intervals and
 * supports operation like union, intersection, add and substract.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class IntervalsListUnitTest {

    /**
     * Test that the defined {@link IntervalsList} iList contains the specified
     * timestamps & intervals. Test that exotic intervals are well handled (null
     * border means infinity).
     */
    @Test
    public void testContains() {
        try {
            IntervalsList iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf("16:00", "16:15"),
                    UnitTestUtils.timeIntervalOf("16:45", "17:00"));

            Assert.assertFalse(iList.contains((Instant) null));
            Assert.assertFalse(iList.contains(UnitTestUtils.timestampOf("16:30")));
            Assert.assertTrue(iList.contains(UnitTestUtils.timestampOf("16:00")));
            Assert.assertTrue(iList.contains(UnitTestUtils.timestampOf("16:50")));

            iList = new IntervalsList(UnitTestUtils.timeIntervalOf("16:15", "16:45"));

            Assert.assertFalse(iList.contains((TimeInterval) null));
            Assert.assertFalse(iList.contains(UnitTestUtils.timeIntervalOf("16:00", "16:30")));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("16:15", "16:45")));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("16:20", "16:30")));

            iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf(null, "16:15"),
                    UnitTestUtils.timeIntervalOf("16:45", null));
            Assert.assertFalse(iList.contains(UnitTestUtils.timeIntervalOf("16:00", "16:30")));
            Assert.assertFalse(iList.contains(UnitTestUtils.timeIntervalOf("16:30", null)));
            Assert.assertFalse(iList.contains(UnitTestUtils.timeIntervalOf(null, "16:30")));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("15:00", "16:00")));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("17:00", "17:10")));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("17:00", null)));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf(null, "16:00")));

        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that the defined {@link IntervalsList} iList intersects the
     * specified timestamps & intervals. Test that exotic intervals are well
     * handled (null border means infinity).
     */
    @Test
    public void testIntersects() {
        try {
            IntervalsList iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf("16:00", "16:15"),
                    UnitTestUtils.timeIntervalOf("16:45", "17:00"));

            Assert.assertFalse(iList.intersects((TimeInterval) null));
            Assert.assertFalse(iList.intersects(UnitTestUtils.timeIntervalOf("16:20", "16:40")));
            Assert.assertTrue(iList.intersects(UnitTestUtils.timeIntervalOf("16:05", "16:10")));
            Assert.assertTrue(iList.intersects(UnitTestUtils.timeIntervalOf("16:15", "16:20")));

            iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf(null, "16:15"),
                    UnitTestUtils.timeIntervalOf("16:45", null));
            Assert.assertFalse(iList.intersects(UnitTestUtils.timeIntervalOf("16:20", "16:40")));
            Assert.assertTrue(iList.intersects(UnitTestUtils.timeIntervalOf("16:05", "16:10")));
            Assert.assertTrue(iList.intersects(UnitTestUtils.timeIntervalOf("16:15", "16:20")));
            Assert.assertTrue(iList.intersects(UnitTestUtils.timeIntervalOf("16:30", null)));
            Assert.assertTrue(iList.intersects(UnitTestUtils.timeIntervalOf(null, "16:30")));

        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that the defined {@link IntervalsList} iList contains the specified
     * timestamps/intervals after adding timestamps/intervals. Test that exotic
     * intervals are well handled (null border means infinity).
     */
    @Test
    public void testAdd() {
        try {
            IntervalsList iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf("16:00", "16:15"),
                    UnitTestUtils.timeIntervalOf("16:45", "17:00"));

            iList.addToSelf((TimeInterval) null);
            iList.addToSelf((IntervalsList) null);
            Assert.assertFalse(iList.contains(UnitTestUtils.timeIntervalOf("16:15", "16:45")));

            iList.addToSelf(UnitTestUtils.timeIntervalOf("16:15", "16:45"));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("16:10", "16:50")));
            Assert.assertEquals(1, iList.getSize());

            iList.addToSelf(new IntervalsList(
                    UnitTestUtils.timeIntervalOf("15:00", "16:00"),
                    UnitTestUtils.timeIntervalOf("17:00", "18:00")));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("15:10", "17:50")));
            Assert.assertEquals(1, iList.getSize());

            iList.addToSelf(UnitTestUtils.timeIntervalOf("16:45", null));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("20:00", "21:00")));
            Assert.assertEquals(1, iList.getSize());

            iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf(null, "16:15"),
                    UnitTestUtils.timeIntervalOf("16:45", null));
            iList.addToSelf(UnitTestUtils.timeIntervalOf("16:15", "16:45"));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf(null, null)));

            iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf("16:00", "16:15"),
                    UnitTestUtils.timeIntervalOf("16:45", "17:00"));
            iList.addToSelf(UnitTestUtils.timeIntervalOf("16:30", null));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("16:40", "16:45")));
            Assert.assertEquals(2, iList.getSize());

        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test that the defined {@link IntervalsList} iList contains the specified
     * timestamps/intervals after substracting timestamps/intervals. Test that
     * exotic intervals are well handled (null border means infinity).
     */
    @Test
    public void testSubstract() {
        try {
            IntervalsList iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf("16:00", "17:00"));

            iList.subtractFromSelf((TimeInterval) null);
            iList.subtractFromSelf((IntervalsList) null);
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("16:15", "16:45")));

            iList.subtractFromSelf(UnitTestUtils.timeIntervalOf("16:15", "16:45"));
            Assert.assertFalse(iList.contains(UnitTestUtils.timeIntervalOf("16:10", "16:50")));
            Assert.assertEquals(2, iList.getSize());

            iList = new IntervalsList(
                    UnitTestUtils.timeIntervalOf("16:00", "17:00"));
            iList.subtractFromSelf(UnitTestUtils.timeIntervalOf("16:00", "17:00"));
            Assert.assertEquals(0, iList.getSize());

            iList.subtractFromSelf(new IntervalsList(
                    UnitTestUtils.timeIntervalOf("15:00", "16:15"),
                    UnitTestUtils.timeIntervalOf("16:45", "18:00")));
            Assert.assertFalse(iList.contains(UnitTestUtils.timeIntervalOf("16:00", "16:15")));
            Assert.assertEquals(0, iList.getSize());

            iList = new IntervalsList(UnitTestUtils.timeIntervalOf("16:00", "17:00"));
            iList.subtractFromSelf(UnitTestUtils.timeIntervalOf(null, "16:15"));
            iList.subtractFromSelf(UnitTestUtils.timeIntervalOf("16:45", null));
            Assert.assertTrue(iList.contains(UnitTestUtils.timeIntervalOf("16:16", "16:44")));
            Assert.assertEquals(1, iList.getSize());

            iList.subtractFromSelf(UnitTestUtils.timeIntervalOf(null, null));
            Assert.assertEquals(0, iList.getSize());

        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

}

/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.TimeStamp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carcassi
 */
public class TimeTest {

    public TimeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void nanoSecCarry() {
        TimeStamp time = TimeStamp.epicsTime(100, 100000000);
        TimeStamp newTime = time.plus(TimeDuration.nanos(999000000));
        assertEquals(TimeStamp.epicsTime(101, 99000000), newTime);
    }

}
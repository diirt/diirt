/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.CoreMatchers.*;


/**
 *
 * @author carcassi
 */
public class StatisticsUtilTest {
    
    @Test
    public void statisticsOf1() {
        Statistics stats = StatisticsUtil.statisticsOf(new ArrayDouble(1.0));
        assertThat(stats.getAverage(), equalTo(1.0));
        assertThat(stats.getStdDev(), equalTo(0.0));
        assertThat(stats.getMinimum(), equalTo((Number) 1.0));
        assertThat(stats.getMaximum(), equalTo((Number) 1.0));
        assertThat(stats.getCount(), equalTo(1));
    }
}

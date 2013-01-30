/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class AxisRangesTest {
    
    public AxisRangesTest() {
    }

    @Test
    public void absolute1() {
        AxisRange axisRange = AxisRanges.absolute(0.0, 10.0);
        Range range = axisRange.axisRange(RangeUtil.range(3.0, 15.0));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 10.0));
    }

    @Test(expected=IllegalArgumentException.class)
    public void absolute2() {
        AxisRange axisRange = AxisRanges.absolute(10.0, 0.0);
    }

    @Test
    public void relative1() {
        AxisRange axisRange = AxisRanges.relative();
        Range range = axisRange.axisRange(RangeUtil.range(3.0, 15.0));
        assertThat(range.getMinimum(), equalTo((Number) 3.0));
        assertThat(range.getMaximum(), equalTo((Number) 15.0));

        range = axisRange.axisRange(RangeUtil.range(1.0, 5.0));
        assertThat(range.getMinimum(), equalTo((Number) 1.0));
        assertThat(range.getMaximum(), equalTo((Number) 5.0));
    }

    @Test
    public void integrated1() {
        AxisRange axisRange = AxisRanges.integrated();
        Range range = axisRange.axisRange(RangeUtil.range(3.0, 15.0));
        assertThat(range.getMinimum(), equalTo((Number) 3.0));
        assertThat(range.getMaximum(), equalTo((Number) 15.0));

        range = axisRange.axisRange(RangeUtil.range(1.0, 5.0));
        assertThat(range.getMinimum(), equalTo((Number) 1.0));
        assertThat(range.getMaximum(), equalTo((Number) 15.0));
    }
}

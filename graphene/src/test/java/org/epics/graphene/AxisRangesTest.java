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
    public void axisRanges1() {
        AxisRange axisRange = AxisRanges.axisRange(0.0, 10.0);
        Range range = axisRange.axisRange(RangeUtil.range(3.0, 15.0));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 10.0));
    }
}

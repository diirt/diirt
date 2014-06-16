/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import org.epics.util.stats.Range;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.graphene.RangeUtil.*;

/**
 *
 * @author carcassi
 */
public class RangeUtilTest {
    
    public RangeUtilTest() {
    }
    
    @Test
    public void range1() throws Exception {
        Range range = RangeUtil.range(0.0, 10.0);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 10.0));
    }
    
    public void range2() throws Exception {
        Range range = RangeUtil.range(0.0, 0.0);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 0.0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void range3() throws Exception {
        Range range = RangeUtil.range(10.0, 0.0);
    }
    
    public void absRange1() {
        Range range = absRange(range(1, 3));
        assertThat(range.getMinimum(), equalTo((Number) 1.0));
        assertThat(range.getMaximum(), equalTo((Number) 3.0));
    }
    
    public void absRange2() {
        Range range = absRange(range(0, 3));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 3.0));
    }
    
    public void absRange3() {
        Range range = absRange(range(-2, 3));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 3.0));
    }
    
    public void absRange4() {
        Range range = absRange(range(-4, 2));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 4.0));
    }
    
    public void absRange5() {
        Range range = absRange(range(-4, -2));
        assertThat(range.getMinimum(), equalTo((Number) 2.0));
        assertThat(range.getMaximum(), equalTo((Number) 4.0));
    }
    
    @Test
    public void subrange1() {
        assertThat(RangeUtil.contains(RangeUtil.range(0.0, 1.0), RangeUtil.range(0.5, 0.75)), equalTo(true));
        assertThat(RangeUtil.contains(RangeUtil.range(0.0, 1.0), RangeUtil.range(0.5, 1.0)), equalTo(true));
        assertThat(RangeUtil.contains(RangeUtil.range(0.0, 1.0), RangeUtil.range(0.0, 0.75)), equalTo(true));
        assertThat(RangeUtil.contains(RangeUtil.range(0.0, 1.0), RangeUtil.range(-1.0, 0.75)), equalTo(false));
        assertThat(RangeUtil.contains(RangeUtil.range(0.0, 1.0), RangeUtil.range(0.0, 1.75)), equalTo(false));
    }
    
    @Test
    public void sum1() {
        Range range1 = RangeUtil.range(0.0, 5.0);
        Range range2 = RangeUtil.range(1.0, 2.0);
        assertThat(RangeUtil.sum(range1, range2), sameInstance(range1));
        assertThat(RangeUtil.sum(range2, range1), sameInstance(range1));
    }
    
    @Test
    public void sum2() {
        Range range1 = RangeUtil.range(0.0, 5.0);
        Range range2 = RangeUtil.range(1.0, 6.0);
        Range range = RangeUtil.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = RangeUtil.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum3() {
        Range range1 = RangeUtil.range(0.0, 3.0);
        Range range2 = RangeUtil.range(4.0, 6.0);
        Range range = RangeUtil.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = RangeUtil.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum4() {
        Range range1 = RangeUtil.range(0.0, 3.0);
        Range range2 = RangeUtil.range(0.0, 6.0);
        Range range = RangeUtil.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = RangeUtil.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum5() {
        Range range1 = RangeUtil.range(0.0, 6.0);
        Range range2 = RangeUtil.range(3.0, 6.0);
        Range range = RangeUtil.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = RangeUtil.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
}

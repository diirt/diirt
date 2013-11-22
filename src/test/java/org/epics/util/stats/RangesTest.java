/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.stats;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class RangesTest {
    
    public RangesTest() {
    }
    
    @Test
    public void range1() throws Exception {
        Range range = Ranges.range(0.0, 10.0);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 10.0));
    }
    
    public void range2() throws Exception {
        Range range = Ranges.range(0.0, 0.0);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 0.0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void range3() throws Exception {
        Range range = Ranges.range(10.0, 0.0);
    }
    
    @Test
    public void subrange1() {
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(0.5, 0.75)), equalTo(true));
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(0.5, 1.0)), equalTo(true));
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(0.0, 0.75)), equalTo(true));
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(-1.0, 0.75)), equalTo(false));
        assertThat(Ranges.contains(Ranges.range(0.0, 1.0), Ranges.range(0.0, 1.75)), equalTo(false));
    }
    
    @Test
    public void sum1() {
        Range range1 = Ranges.range(0.0, 5.0);
        Range range2 = Ranges.range(1.0, 2.0);
        assertThat(Ranges.sum(range1, range2), sameInstance(range1));
        assertThat(Ranges.sum(range2, range1), sameInstance(range1));
    }
    
    @Test
    public void sum2() {
        Range range1 = Ranges.range(0.0, 5.0);
        Range range2 = Ranges.range(1.0, 6.0);
        Range range = Ranges.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = Ranges.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum3() {
        Range range1 = Ranges.range(0.0, 3.0);
        Range range2 = Ranges.range(4.0, 6.0);
        Range range = Ranges.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = Ranges.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum4() {
        Range range1 = Ranges.range(0.0, 3.0);
        Range range2 = Ranges.range(0.0, 6.0);
        Range range = Ranges.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = Ranges.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
    
    @Test
    public void sum5() {
        Range range1 = Ranges.range(0.0, 6.0);
        Range range2 = Ranges.range(3.0, 6.0);
        Range range = Ranges.sum(range1, range2);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
        range = Ranges.sum(range2, range1);
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 6.0));
    }
}

/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.stats;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class RangeTest {
    
    @Test
    public void range1() throws Exception {
        Range range = Range.create(0.0, 10.0);
        assertThat(range.getMinimum(), equalTo(0.0));
        assertThat(range.getMaximum(), equalTo(10.0));
        assertThat(range.isReversed(), equalTo(false));
        assertThat(range.toString(), equalTo("[0.0 - 10.0]"));
    }
    
    @Test
    public void range2() throws Exception {
        Range range = Range.create(0.0, 0.0);
        assertThat(range.getMinimum(), equalTo(0.0));
        assertThat(range.getMaximum(), equalTo(0.0));
        assertThat(range.isReversed(), equalTo(false));
        assertThat(range.toString(), equalTo("[0.0 - 0.0]"));
    }
    
    @Test
    public void range3() throws Exception {
        Range range = Range.create(10.0, 0.0);
        assertThat(range.getMinimum(), equalTo(0.0));
        assertThat(range.getMaximum(), equalTo(10.0));
        assertThat(range.isReversed(), equalTo(true));
        assertThat(range.toString(), equalTo("[10.0 - 0.0]"));
    }
    
    @Test
    public void range4() throws Exception {
        Range range = Range.create(0.0, Double.NaN);
        assertThat(range, sameInstance(Range.undefined()));
    }
    
    @Test
    public void equal1() throws Exception {
        assertThat(Range.create(0.0, 10.0), equalTo(Range.create(0.0, 10.0)));
        assertThat(Range.create(10.0, 0.0), not(equalTo(Range.create(0.0, 10.0))));
        assertThat(Range.create(10.0, 0.0), not(equalTo(Range.create(1.0, 10.0))));
        assertThat(Range.create(10.0, 0.0), not(equalTo(null)));
        assertThat(Range.undefined(), equalTo(Range.undefined()));
    }
    
}

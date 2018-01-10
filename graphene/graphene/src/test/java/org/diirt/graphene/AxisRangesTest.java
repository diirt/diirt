/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import org.diirt.util.stats.Ranges;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class AxisRangesTest {

    public AxisRangesTest() {
    }

    @Test
    public void fixed1() {
        AxisRange axisRange = AxisRanges.fixed(0.0, 10.0);
        assertThat(axisRange.toString(), equalTo("fixed(0.0, 10.0)"));
        AxisRangeInstance axisRangeInstance = axisRange.createInstance();
        Range range = axisRangeInstance.axisRange(Ranges.range(3.0, 15.0), Ranges.range(-3.0, 4.0));
        assertThat(range.getMinimum(), equalTo((Number) 0.0));
        assertThat(range.getMaximum(), equalTo((Number) 10.0));
    }

    @Test(expected=IllegalArgumentException.class)
    public void fixed2() {
        AxisRange axisRange = AxisRanges.fixed(10.0, 0.0);
    }

    @Test
    public void relative1() {
        AxisRange axisRange = AxisRanges.data();
        assertThat(axisRange.toString(), equalTo("data"));
        AxisRangeInstance axisRangeInstance = axisRange.createInstance();
        Range range = axisRangeInstance.axisRange(Ranges.range(3.0, 15.0), Ranges.range(-3.0, 4.0));
        assertThat(range.getMinimum(), equalTo((Number) 3.0));
        assertThat(range.getMaximum(), equalTo((Number) 15.0));

        range = axisRangeInstance.axisRange(Ranges.range(1.0, 5.0), Ranges.range(-3.0, 4.0));
        assertThat(range.getMinimum(), equalTo((Number) 1.0));
        assertThat(range.getMaximum(), equalTo((Number) 5.0));
    }

    @Test
    public void auto1() {
        AxisRange axisRange = AxisRanges.auto();
        assertThat(axisRange.toString(), equalTo("auto(80%)"));
        AxisRangeInstance axisRangeInstance = axisRange.createInstance();
        Range range = axisRangeInstance.axisRange(Ranges.range(3.0, 5.0), Ranges.range(-3.0, 4.0));
        assertThat(range.getMinimum(), equalTo((Number) 3.0));
        assertThat(range.getMaximum(), equalTo((Number) 5.0));

        range = axisRangeInstance.axisRange(Ranges.range(4.0, 15.0), Ranges.range(-3.0, 4.0));
        assertThat(range.getMinimum(), equalTo((Number) 3.0));
        assertThat(range.getMaximum(), equalTo((Number) 15.0));
    }

    @Test
    public void auto2() {
        AxisRange axisRange = AxisRanges.auto();
        assertThat(axisRange.toString(), equalTo("auto(80%)"));
        AxisRangeInstance axisRangeInstance = axisRange.createInstance();
        Range range = axisRangeInstance.axisRange(Ranges.range(3.0, 5.0), Ranges.range(-3.0, 4.0));
        assertThat(range.getMinimum(), equalTo((Number) 3.0));
        assertThat(range.getMaximum(), equalTo((Number) 5.0));

        range = axisRangeInstance.axisRange(Ranges.range(1000000.0, 1000015.0), Ranges.range(-3.0, 4.0));
        assertThat(range.getMinimum(), equalTo((Number) 1000000.0));
        assertThat(range.getMaximum(), equalTo((Number) 1000015.0));
    }

    @Test
    public void display1() {
        AxisRange axisRange = AxisRanges.display();
        assertThat(axisRange.toString(), equalTo("display"));
        AxisRangeInstance axisRangeInstance = axisRange.createInstance();
        Range range = axisRangeInstance.axisRange(Ranges.range(3.0, 15.0), Ranges.range(-3.0, 4.0));
        assertThat(range.getMinimum(), equalTo((Number) (-3.0)));
        assertThat(range.getMaximum(), equalTo((Number) 4.0));
    }

    @Test
    public void display2() {
        AxisRangeInstance axisRangeInstance = AxisRanges.display().createInstance();
        Range range = axisRangeInstance.axisRange(Ranges.range(3.0, 15.0), Ranges.range(0.0, 0.0));
        assertThat(range.getMinimum(), equalTo((Number) 3.0));
        assertThat(range.getMaximum(), equalTo((Number) 15.0));
    }

    @Test
    public void fixedEquals() {
        assertThat(AxisRanges.fixed(0, 1), equalTo(AxisRanges.fixed(0, 1)));
        assertThat(AxisRanges.fixed(0, 1), not(equalTo(AxisRanges.fixed(0, 5))));
    }

    @Test
    public void displayEquals() {
        assertThat(AxisRanges.display(), equalTo(AxisRanges.display()));
    }

    @Test
    public void dataEquals() {
        assertThat(AxisRanges.data(), equalTo(AxisRanges.data()));
    }

    @Test
    public void autoEquals() {
        assertThat(AxisRanges.auto(), equalTo(AxisRanges.auto()));
        assertThat(AxisRanges.auto(0.5), equalTo(AxisRanges.auto(0.5)));
        assertThat(AxisRanges.auto(0.5), not(equalTo(AxisRanges.auto(0.8))));
    }
}

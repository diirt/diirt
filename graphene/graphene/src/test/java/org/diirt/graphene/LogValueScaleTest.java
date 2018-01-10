/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.graphene.LinearValueScaleTest.assertAxisEquals;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListDouble;
import org.diirt.util.stats.Ranges;
import org.junit.Ignore;

/**
 *
 * @author carcassi
 */
public class LogValueScaleTest {

    @Test
    public void scaleValue1() {
        ValueScale logScale = ValueScales.logScale();
        assertThat(logScale.scaleValue(10, 1, 100, 0, 100), equalTo(50.0));
    }

    @Test
    public void scaleValue2() {
        ValueScale logScale = ValueScales.logScale();
        assertThat(logScale.scaleValue(100, 10, 1000, -100, 100), equalTo(0.0));
    }

    @Test
    public void invScaleValue1() {
        ValueScale logScale = ValueScales.logScale();
        assertThat(logScale.invScaleValue(50.0, 1, 100, 0, 100), equalTo(10.0));
    }

    @Test
    public void invScaleValue2() {
        ValueScale logScale = ValueScales.logScale();
        assertThat(logScale.invScaleValue(0.0, 10, 1000, -100, 100), equalTo(100.0));
    }

    @Test
    public void references1() {
        ValueScale logScale = ValueScales.logScale();
        ValueAxis axis = logScale.references(Ranges.range(1.0, 1000.0), 2, 4);
        assertAxisEquals(1.0, 1000.0, new double[]{1.0, 10.0, 100.0, 1000.0}, new String[]{"1", "10", "100", "1000"}, axis);
    }

    @Test
    public void references2() {
        ValueScale logScale = ValueScales.logScale();
        ValueAxis axis = logScale.references(Ranges.range(100.0, 100000.0), 2, 4);
        assertAxisEquals(100.0, 100000.0, new double[]{100.0, 1000.0, 10000.0, 100000.0}, new String[]{"1e2", "1e3", "1e4", "1e5"}, axis);
    }

    @Test
    public void references3() {
        ValueScale logScale = ValueScales.logScale();
        ValueAxis axis = logScale.references(Ranges.range(0.001, 1), 2, 4);
        assertAxisEquals(0.001, 1, new double[]{0.001, 0.01, 0.1, 1.0}, new String[]{"0.001", "0.010", "0.100", "1.000"}, axis);
    }

    @Test
    public void references4() {
        ValueScale logScale = ValueScales.logScale();
        ValueAxis axis = logScale.references(Ranges.range(0.0001, 1), 2, 7);
        assertAxisEquals(0.0001, 1, new double[]{0.0001, 0.001, 0.01, 0.1, 1.0}, new String[]{"1e-4", "1e-3", "1e-2", "1e-1", "1e0"}, axis);
    }

    @Test
    public void references5() {
        ValueScale logScale = ValueScales.logScale();
        ValueAxis axis = logScale.references(Ranges.range(5, 50), 2, 7);
        assertAxisEquals(5, 50, new double[]{6,8,10,20,40}, new String[]{"6", "8", "10", "20", "40"}, axis);
    }

    @Test
    public void references6() {
        ValueScale logScale = ValueScales.logScale();
        ValueAxis axis = logScale.references(Ranges.range(100, 1000), 2, 12);
        assertAxisEquals(100, 1000, new double[]{100,200,300,400,500,600,700,800,900,1000}, new String[]{"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"}, axis);
    }

    @Test
    @Ignore
    public void references7() {
        ValueScale logScale = ValueScales.logScale();
        ValueAxis axis = logScale.references(Ranges.range(100, 100.1), 2, 6);
        assertAxisEquals(100, 100.1, new double[]{100.00,100.02,100.04,100.06,100.08,100.10}, new String[]{"100.00", "100.02", "100.04", "100.06", "100.08", "100.10"}, axis);
    }

    @Test
    public void generateReferenceValues1() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(1, 10), 1), equalTo((ListDouble) new ArrayDouble(1, 10)));
    }

    @Test
    public void generateReferenceValues2() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(1, 10), 2), equalTo((ListDouble) new ArrayDouble(1, 5, 10)));
    }

    @Test
    public void generateReferenceValues3() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(1, 10), 5), equalTo((ListDouble) new ArrayDouble(1, 2, 4, 6, 8, 10)));
    }

    @Test
    public void generateReferenceValues4() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(1, 10), 10), equalTo((ListDouble) new ArrayDouble(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
    }

    @Test
    public void generateReferenceValues5() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(1, 3), 10), equalTo((ListDouble) new ArrayDouble(1, 2, 3)));
    }

    @Test
    public void generateReferenceValues6() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(1, 1.5), 100), equalTo((ListDouble) new ArrayDouble(1, 1.1, 1.2, 1.3, 1.4, 1.5)));
    }

    @Test
    public void generateReferenceValues7() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(1, 10000), 1), equalTo((ListDouble) new ArrayDouble(1, 10, 100, 1000, 10000)));
    }

    @Test
    public void generateReferenceValues8() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(1, 10000), 2),
                equalTo((ListDouble) new ArrayDouble(1, 5, 10, 50, 100, 500, 1000, 5000, 10000)));
    }

    @Test
    public void generateReferenceValues9() {
        assertThat(LogValueScale.generateReferenceValues(Ranges.range(5, 50), 5),
                equalTo((ListDouble) new ArrayDouble(6,8,10,20,40)));
    }

    @Test
    public void quantize1() {
        assertThat(LogValueScale.quantize(0.00045), equalTo(1));
    }

    @Test
    public void quantize2() {
        assertThat(LogValueScale.quantize(1.2), equalTo(2));
    }

    @Test
    public void quantize3() {
        assertThat(LogValueScale.quantize(33.5), equalTo(50));
    }

    @Test
    public void quantize4() {
        assertThat(LogValueScale.quantize(99.99), equalTo(100));
    }

    @Test
    public void decreaseFactor1() {
        assertThat(LogValueScale.decreaseFactor(200), equalTo(100));
    }

    @Test
    public void decreaseFactor2() {
        assertThat(LogValueScale.decreaseFactor(100), equalTo(50));
    }

    @Test
    public void decreaseFactor3() {
        assertThat(LogValueScale.decreaseFactor(1), equalTo(1));
    }

    @Test
    public void decreaseFactor4() {
        assertThat(LogValueScale.decreaseFactor(5), equalTo(2));
    }
}
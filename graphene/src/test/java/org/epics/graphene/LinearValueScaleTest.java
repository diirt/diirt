/**
 * Copyright (C) 2012 Brookhaven National Laboratory All rights reserved. Use is
 * subject to license terms.
 */
package org.epics.graphene;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class LinearValueScaleTest {

    @Test
    public void scaleValue1() {
        ValueScale linearScale = ValueScales.linearScale();
        assertThat(linearScale.scaleValue(3.5, 3, 4, 0, 100), equalTo(50.0));
    }

    @Test
    public void scaleValue2() {
        ValueScale linearScale = ValueScales.linearScale();
        assertThat(linearScale.scaleValue(3.5, 3, 4, -100, 100), equalTo(0.0));
    }

    @Test
    public void testTicksForRange1() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(1.0, 9.0), 2, 4);
        assertAxisEquals(1.0, 9.0, new double[]{2.0, 4.0, 6.0, 8.0}, new String[]{"2", "4", "6", "8"}, axis);
    }

    @Test
    public void testTicksForRange2() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.0, 10.0), 2, 11);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0},
                new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, axis);
    }

    @Test
    public void testTicksForRange3() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.0, 10.0), 2, 21);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0},
                new String[]{"0.0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5",
            "5.0", "5.5", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "9.0", "9.5", "10.0"}, axis);
    }
//    
//    @Test
//    public void testTicksForRange4() {
//         ValueScale linearScale = ValueScales.linearScale();
//         ValueAxis axis = linearScale.references(RangeUtil.range(0.0, 10.0), 2, 101, 1.0);
//        assertAxisEquals(0.0, 10.0, new double[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0},
//                new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, axis);
//    }

    @Test
    public void testTicksForRange5() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.0, 10.0), 2, 6);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 2.0, 4.0, 6.0, 8.0, 10.0},
                new String[]{"0", "2", "4", "6", "8", "10"}, axis);
    }

    @Test
    public void testTicksForRange6() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.0, 10.0), 2, 8);
        assertAxisEquals(0.0, 10.0, new double[]{0.0, 2.0, 4.0, 6.0, 8.0, 10.0},
                new String[]{"0", "2", "4", "6", "8", "10"}, axis);
    }

    @Test
    public void testTicksForRange7() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(-10.0, -1.0), 2, 11);
        assertAxisEquals(-10.0, -1.0, new double[]{-10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0},
                new String[]{"-10", "-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1"}, axis);
    }

    @Test
    public void testTicksForRange8() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(-10.0, 0.0), 2, 11);
        assertAxisEquals(-10.0, 0.0, new double[]{-10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 0.0},
                new String[]{"-10", "-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1", "0"}, axis);
    }

    @Test
    public void testTicksForRange9() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.9, 1.3), 2, 10);
        assertAxisEquals(0.9, 1.3, new double[]{0.9, 0.95, 1.0, 1.05, 1.1, 1.15, 1.2, 1.25, 1.3},
                new String[]{"0.90", "0.95", "1.00", "1.05", "1.10", "1.15", "1.20", "1.25", "1.30"}, axis);
    }

    @Test
    public void testTicksForRange10() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.77777, 0.88888), 2, 15);
        assertAxisEquals(0.77777, 0.88888, new double[]{0.78, 0.79, 0.80, 0.81, 0.82, 0.83, 0.84, 0.85, 0.86, 0.87, 0.88},
                new String[]{"0.78", "0.79", "0.80", "0.81", "0.82", "0.83", "0.84", "0.85", "0.86", "0.87", "0.88"}, axis);
    }

    @Test
    public void testTicksForRange11() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(100.77777, 100.88888), 2, 15);
        assertAxisEquals(100.77777, 100.88888, new double[]{100.78, 100.79, 100.80, 100.81, 100.82, 100.83, 100.84, 100.85, 100.86, 100.87, 100.88},
                new String[]{"100.78", "100.79", "100.80", "100.81", "100.82", "100.83", "100.84", "100.85", "100.86", "100.87", "100.88"}, axis);
    }

    @Test
    public void testTicksForRange12() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.000077777, 0.000088888), 2, 15);
        assertAxisEquals(0.000077777, 0.000088888, new double[]{0.000078, 0.000079, 0.000080, 0.000081, 0.000082, 0.000083, 0.000084, 0.000085, 0.000086, 0.000087, 0.000088},
                new String[]{"7.8e-5", "7.9e-5", "8.0e-5", "8.1e-5", "8.2e-5", "8.3e-5", "8.4e-5", "8.5e-5", "8.6e-5", "8.7e-5", "8.8e-5"}, axis);
    }

    @Test
    public void testTicksForRange13() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.0000799, 0.0001201), 2, 5);
        assertAxisEquals(0.00008, 0.00012, new double[]{0.00008, 0.00009, 0.0001, 0.00011, 0.00012},
                new String[]{"0.8e-4", "0.9e-4", "1.0e-4", "1.1e-4", "1.2e-4"}, axis);
    }

    @Test
    public void testTicksForRange14() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.0000799, 0.0000809), 2, 5);
        assertAxisEquals(0.0000799, 0.0000809, new double[]{0.00008, 0.0000802, 0.0000804, 0.0000806, 0.0000808},
                new String[]{"8.00e-5", "8.02e-5", "8.04e-5", "8.06e-5", "8.08e-5"}, axis);
    }

    @Test
    public void testTicksForRange15() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.000099, 0.0004001), 2, 5);
        assertAxisEquals(0.000099, 0.0004001, new double[]{0.0001, 0.0002, 0.0003, 0.0004},
                new String[]{"1e-4", "2e-4", "3e-4", "4e-4"}, axis);
    }

    @Test
    public void testTicksForRange16() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(234567, 678967), 2, 5);
        assertAxisEquals(234567, 678967, new double[]{300000, 400000, 500000, 600000},
                new String[]{"3e5", "4e5", "5e5", "6e5"}, axis);
    }
//    
//    @Test
//    public void testTicksForRange17() {
//         ValueScale linearScale = ValueScales.linearScale();
//         ValueAxis axis = linearScale.references(RangeUtil.range(0, 550), 2, 7, 1.0);
//        assertAxisEquals(0, 550, new double[]{0, 100, 200, 300, 400, 500},
//                new String[]{"0","100","200","300", "400", "500"}, axis);
//    }

    @Test
    public void testTicksForRange18() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.00001, 0.00004), 2, 4);
        assertAxisEquals(0.00001, 0.00004, new double[]{0.00001, 0.00002, 0.00003, 0.00004},
                new String[]{"1e-5", "2e-5", "3e-5", "4e-5"}, axis);
    }

    @Test
    public void testTicksForRange19() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(0.00001, 0.0004), 2, 5);
        assertAxisEquals(0.00001, 0.0004, new double[]{0.0001, 0.0002, 0.0003, 0.0004},
                new String[]{"1e-4", "2e-4", "3e-4", "4e-4"}, axis);
    }

    @Test
    public void testTicksForRange20() {
        ValueScale linearScale = ValueScales.linearScale();
        ValueAxis axis = linearScale.references(RangeUtil.range(-2.644, 3.3689), 2, 3);
        assertAxisEquals(-2.644, 3.3689, new double[]{-2.0, 0.0, 2.0},
                new String[]{"-2", "0", "2"}, axis);
    }

    private void assertAxisEquals(double minValue, double maxValue, double[] tickValues, String[] tickLabels, org.epics.graphene.ValueAxis axis) {
        assertEquals(minValue, axis.getMinValue(), 0.000001);
        assertEquals(maxValue, axis.getMaxValue(), 0.000001);
        assertArrayEquals(tickValues, axis.getTickValues(), 0.000001);
        assertArrayEquals(tickLabels, axis.getTickLabels());
    }
}
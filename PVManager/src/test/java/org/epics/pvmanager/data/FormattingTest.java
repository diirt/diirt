/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import org.epics.pvmanager.util.NumberFormats;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.pvmanager.data.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class FormattingTest {

    @Test
    public void testDefaultPrecision() {
        Display display = newDisplay(Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", NumberFormats.format(3), Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
        ValueFormat f = new SimpleValueFormat(3);
        assertThat(f.format(newVDouble(1234.5678, display)), equalTo("1234.568"));
        assertThat(f.format(DataUtils.createValue(null, new int[] {1, 2, 3})), equalTo("[1, 2, 3]"));
        assertThat(f.format(DataUtils.createValue(null, new int[] {1})), equalTo("[1]"));
        assertThat(f.format(DataUtils.createValue(null, new int[] {1, 2, 3, 4, 5})), equalTo("[1, 2, 3, ...]"));
        assertThat(f.format(newVDoubleArray(new double[] {1, 2, 3}, display)), equalTo("[1.000, 2.000, 3.000]"));
        assertThat(f.format(newVDoubleArray(new double[] {1}, display)), equalTo("[1.000]"));
        assertThat(f.format(newVDoubleArray(new double[] {1, 2, 3, 4, 5}, display)), equalTo("[1.000, 2.000, 3.000, ...]"));
    }

    @Test
    public void testMandatedPrecision() {
        Display display = newDisplay(Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", NumberFormats.format(3), Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
        ValueFormat f = new SimpleValueFormat(3);
        f.setNumberFormat(NumberFormats.format(2));
        assertThat(f.format(newVDouble(1234.5678, display)), equalTo("1234.57"));
        assertThat(f.format(DataUtils.createValue(null, new int[] {1, 2, 3})), equalTo("[1.00, 2.00, 3.00]"));
        assertThat(f.format(DataUtils.createValue(null, new int[] {1})), equalTo("[1.00]"));
        assertThat(f.format(DataUtils.createValue(null, new int[] {1, 2, 3, 4, 5})), equalTo("[1.00, 2.00, 3.00, ...]"));
        assertThat(f.format(newVDoubleArray(new double[] {1, 2, 3}, display)), equalTo("[1.00, 2.00, 3.00]"));
        assertThat(f.format(newVDoubleArray(new double[] {1}, display)), equalTo("[1.00]"));
        assertThat(f.format(newVDoubleArray(new double[] {1, 2, 3, 4, 5}, display)), equalTo("[1.00, 2.00, 3.00, ...]"));
    }

}
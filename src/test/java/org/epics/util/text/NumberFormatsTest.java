/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.util.text;

import java.text.NumberFormat;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class NumberFormatsTest {
    
    public NumberFormatsTest() {
    }

    @Test
    public void format() {
        NumberFormat format = NumberFormats.format(2);
        assertThat(format.format(2.0), equalTo("2.00"));
        assertThat(format.format(Double.NaN), equalTo("NaN"));
        assertThat(format.format(Double.POSITIVE_INFINITY), equalTo("Infinity"));
        assertThat(format.format(Double.NEGATIVE_INFINITY), equalTo("-Infinity"));
        assertThat(NumberFormats.format(2), sameInstance(format));
    }

    @Test
    public void toStringFormat() {
        NumberFormat format = NumberFormats.toStringFormat();
        assertThat(format.format(2.0), equalTo("2.0"));
        assertThat(format.format(Double.NaN), equalTo("NaN"));
        assertThat(format.format(Double.POSITIVE_INFINITY), equalTo("Infinity"));
        assertThat(format.format(Double.NEGATIVE_INFINITY), equalTo("-Infinity"));
        assertThat(NumberFormats.toStringFormat(), sameInstance(format));
    }
}
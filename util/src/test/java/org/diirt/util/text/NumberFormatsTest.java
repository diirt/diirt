/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.text;

import java.text.NumberFormat;
import static org.hamcrest.CoreMatchers.equalTo;
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
    public void format1() {
        NumberFormat format = NumberFormats.format(2);
        assertThat(format.format(2.0), equalTo("2.00"));
        assertThat(format.format(Double.NaN), equalTo("NaN"));
        assertThat(format.format(Double.POSITIVE_INFINITY), equalTo("Infinity"));
        assertThat(format.format(Double.NEGATIVE_INFINITY), equalTo("-Infinity"));
        assertThat(NumberFormats.format(2), sameInstance(format));
    }

    @Test
    public void format2() {
        NumberFormat f = NumberFormats.format(3);
        assertThat(f.format(1234.4567), equalTo("1234.457"));
        assertThat(f.format(123), equalTo("123.000"));
        assertThat(f.format(123.4), equalTo("123.400"));

        f = NumberFormats.format(0);
        assertThat(f.format(1234.4567), equalTo("1234"));
        assertThat(f.format(123), equalTo("123"));
        assertThat(f.format(123.4), equalTo("123"));

        f = NumberFormats.format(4);
        assertThat(f.format(1234.4567), equalTo("1234.4567"));
        assertThat(f.format(123), equalTo("123.0000"));
        assertThat(f.format(123.4), equalTo("123.4000"));
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
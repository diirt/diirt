/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class MathIgnoreNaNTest {

    public MathIgnoreNaNTest() {
    }

    @Test
    public void testMin1() {
        double result = MathIgnoreNaN.min(1, 2);
        assertThat(result, equalTo(1.0));
    }

    @Test
    public void testMin2() {
        double result = MathIgnoreNaN.min(1, Double.NaN);
        assertThat(result, equalTo(1.0));
    }

    @Test
    public void testMin3() {
        double result = MathIgnoreNaN.min(Double.NaN, 2);
        assertThat(result, equalTo(2.0));
    }

    @Test
    public void testMax1() {
        double result = MathIgnoreNaN.max(1, 2);
        assertThat(result, equalTo(2.0));
    }

    @Test
    public void testMax2() {
        double result = MathIgnoreNaN.max(1, Double.NaN);
        assertThat(result, equalTo(1.0));
    }

    @Test
    public void testMax3() {
        double result = MathIgnoreNaN.max(Double.NaN, 2);
        assertThat(result, equalTo(2.0));
    }
}

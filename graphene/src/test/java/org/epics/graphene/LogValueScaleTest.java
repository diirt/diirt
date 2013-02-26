/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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
}
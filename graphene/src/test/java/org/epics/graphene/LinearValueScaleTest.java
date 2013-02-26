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
}
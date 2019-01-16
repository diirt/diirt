/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.epics.vtype.VDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests gaussian sim function
 *
 * @author carcassi
 */
public class GaussianNoiseTest {

    /**
     * Tests values follow the right distribution
     */
    @Test
    public void values() {
        // Creates the function
        GaussianNoise gaussian = new GaussianNoise(10.0, 10.0, 1.0);
        VDouble firstValue = gaussian.nextValue();

        // Check limits
        assertThat(firstValue.getDisplay().getControlRange().getMinimum(), equalTo(-30.0));
        assertThat(firstValue.getDisplay().getDisplayRange().getMinimum(), equalTo(-30.0));
        assertThat(firstValue.getDisplay().getAlarmRange().getMinimum(), equalTo(-10.0));
        assertThat(firstValue.getDisplay().getWarningRange().getMinimum(), equalTo(0.0));
        assertThat(firstValue.getDisplay().getWarningRange().getMaximum(), equalTo(20.0));
        assertThat(firstValue.getDisplay().getAlarmRange().getMaximum(), equalTo(30.0));
        assertThat(firstValue.getDisplay().getDisplayRange().getMaximum(), equalTo(50.0));
        assertThat(firstValue.getDisplay().getControlRange().getMaximum(), equalTo(50.0));

        // Calculate histogram
        int quart1 = 0;
        int quart2 = 0;
        int quart3 = 0;
        int quart4 = 0;

        for (int i = 0; i < 100000; i++) {
            double value = gaussian.nextValue().getValue();
            if (value < 10.0) {
                if (value < 0.0) {
                    quart1++;
                } else {
                    quart2++;
                }
            } else {
                if (value < 20.0) {
                    quart3++;
                } else {
                    quart4++;
                }
            }
        }

        // Check distribution
        // Theoretical distribution 15.87% 34.13% 34.13% 15.87%
        assertTrue(quart1 < 16870);
        assertTrue(quart2 < 35130);
        assertTrue(quart3 < 35130);
        assertTrue(quart4 < 16870);
        assertEquals(100000, quart1+quart2+quart3+quart4);
    }
}

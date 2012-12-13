/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import org.epics.pvmanager.data.VDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests gaussian sim function
 *
 * @author carcassi
 */
public class GaussianTest {

    /**
     * Tests values follow the right distribution
     */
    @Test
    public void values() {
        // Creates the function
        Gaussian gaussian = new Gaussian(10.0, 10.0, 1.0);
        VDouble firstValue = gaussian.nextValue();

        // Check limits
        assertThat(firstValue.getLowerCtrlLimit(), equalTo(-30.0));
        assertThat(firstValue.getLowerDisplayLimit(), equalTo(-30.0));
        assertThat(firstValue.getLowerAlarmLimit(), equalTo(-10.0));
        assertThat(firstValue.getLowerWarningLimit(), equalTo(0.0));
        assertThat(firstValue.getUpperWarningLimit(), equalTo(20.0));
        assertThat(firstValue.getUpperAlarmLimit(), equalTo(30.0));
        assertThat(firstValue.getUpperDisplayLimit(), equalTo(50.0));
        assertThat(firstValue.getUpperCtrlLimit(), equalTo(50.0));

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

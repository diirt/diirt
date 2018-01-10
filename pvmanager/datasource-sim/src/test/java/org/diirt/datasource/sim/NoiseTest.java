/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.diirt.vtype.VDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests uniform noise distribution function
 *
 * @author carcassi
 */
public class NoiseTest {

    @Test
    public void values() {
        // Creates the function
        Noise noise = new Noise(-10.0, 10.0, 1.0);
        VDouble firstValue = noise.nextValue();

        // Check limits
        assertThat(firstValue.getLowerCtrlLimit(), equalTo(-10.0));
        assertThat(firstValue.getLowerDisplayLimit(), equalTo(-10.0));
        assertThat(firstValue.getLowerAlarmLimit(), equalTo(-8.0));
        assertThat(firstValue.getLowerWarningLimit(), equalTo(-6.0));
        assertThat(firstValue.getUpperWarningLimit(), equalTo(6.0));
        assertThat(firstValue.getUpperAlarmLimit(), equalTo(8.0));
        assertThat(firstValue.getUpperDisplayLimit(), equalTo(10.0));
        assertThat(firstValue.getUpperCtrlLimit(), equalTo(10.0));

        // Calculate histogram
        int quart1 = 0;
        int quart2 = 0;
        int quart3 = 0;
        int quart4 = 0;

        for (int i = 0; i < 100000; i++) {
            double value = noise.nextValue().getValue();
            if (value < 0) {
                if (value < -5.0) {
                    quart1++;
                } else {
                    quart2++;
                }
            } else {
                if (value < 5.0) {
                    quart3++;
                } else {
                    quart4++;
                }
            }
        }

        // Check distribution
        // Each quarts gets 25%
        assertTrue(quart1 < 26000);
        assertTrue(quart2 < 26000);
        assertTrue(quart3 < 26000);
        assertTrue(quart4 < 26000);
        assertEquals(100000, quart1+quart2+quart3+quart4);
    }
}

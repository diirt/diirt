/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager;

import org.epics.pvmanager.LatestValueCollector;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the LatestValueCollector.
 *
 * @author carcassi
 */
public class LatestValueCollectorTest {

    @Test
    public void inputOutput() throws InterruptedException {
        LatestValueCollector<Integer> collector = new LatestValueCollector<>();
        for (int i = 0; i < 1000; i++) {
            collector.writeValue(i);
            assertThat(collector.readValue(), equalTo(i));
        }
    }
}

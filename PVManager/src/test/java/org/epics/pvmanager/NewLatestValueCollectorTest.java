/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class NewLatestValueCollectorTest {
    
    public NewLatestValueCollectorTest() {
    }

    @Test
    public void inputOutput() throws InterruptedException {
        NewLatestValueCollector<Integer> collector = new NewLatestValueCollector<>();
        for (int i = 0; i < 1000; i++) {
            collector.setValue(i);
            assertThat(collector.getValue(), equalTo(i));
        }
    }
}

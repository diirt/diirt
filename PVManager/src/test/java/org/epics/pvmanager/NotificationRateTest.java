/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.expression.Queue;
import static org.junit.Assert.*;
import org.junit.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.util.time.TimeDuration.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests rate decoupling and throttling.
 *
 * @author carcassi
 */
public class NotificationRateTest {

    public NotificationRateTest() {
    }

    @Before
    public void setUp() {
        pv = null;
    }

    @After
    public void tearDown() {
        if (pv != null) {
            pv.close();
            pv = null;
        }
    }

    private volatile PVReader<?> pv;
    
    @Test
    public void testRateDecoupling() throws Exception {
        Queue<Integer> queue = queueOf(Integer.class, 10);
        CountDownPVReaderListener listener = new CountDownPVReaderListener(1);
        pv = PVManager.read(queue).from(new MockDataSource()).readListener(listener).maxRate(ofMillis(100));
        
        // Wait for connection
        listener.await(ofMillis(200));
        assertThat(listener.getCount(), equalTo(0));
        listener.resetCount(1);
        
        // No new values, should get no new notification
        listener.await(ofMillis(500));
        assertThat(listener.getCount(), equalTo(1));
        
        // Add one value, should get one notification
        queue.add(1);
        listener.await(ofMillis(150));
        assertThat(listener.getCount(), equalTo(0));
        listener.resetCount(1);
        
        // No new values, should get no new notification
        listener.await(ofMillis(500));
        assertThat(listener.getCount(), equalTo(1));
        listener.resetCount(3);
        
        // Add multiple values, should get at max two notification
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
        queue.add(6);
        listener.await(ofMillis(500));
        assertThat(listener.getCount(), not(equalTo(3)));
        assertThat(listener.getCount(), not(equalTo(0)));
        
    }


}
/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.diirt.datasource.test.CountDownPVReaderListener;
import org.diirt.datasource.test.MockDataSource;
import java.util.Arrays;
import java.util.concurrent.Executor;
import org.diirt.datasource.expression.Queue;
import static org.junit.Assert.*;
import org.junit.*;
import static org.diirt.datasource.ExpressionLanguage.*;
import static java.time.Duration.*;
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
    private final Executor delayedExecutor = new Executor() {

        @Override
        public void execute(Runnable command) {
            try {
                Thread.sleep(500);
                command.run();
            } catch(InterruptedException ex) {
                Thread.interrupted();
            }
        }
    };

    @Test
    public void rateThrottling1() throws Exception {
        Queue<Integer> queue = queueOf(Integer.class).maxSize(10);
        CountDownPVReaderListener listener = new CountDownPVReaderListener(1);
        pv = PVManager.read(queue).from(new MockDataSource())
                .notifyOn(delayedExecutor)
                .readListener(listener)
                .maxRate(ofMillis(10));

        // Wait for connection
        listener.await(ofMillis(700));
        assertThat(listener.getCount(), equalTo(0));
        listener.resetCount(1);

        // No new values, should get no new notification
        listener.await(ofMillis(500));
        assertThat(listener.getCount(), equalTo(1));

        // Add one value, notification should not come right away
        queue.add(1);
        listener.await(ofMillis(100));
        assertThat(listener.getCount(), equalTo(1));

        // Add a few other values, still no new notification
        queue.add(2);
        queue.add(3);
        listener.await(ofMillis(100));
        assertThat(listener.getCount(), equalTo(1));
        queue.add(4);

        // Wait longer for first notification
        listener.await(ofMillis(500));
        assertThat(listener.getCount(), equalTo(0));
        assertThat(pv.getValue(), equalTo((Object) Arrays.asList(1)));
        listener.resetCount(1);

        // Wait for second notification
        listener.await(ofMillis(700));
        assertThat(listener.getCount(), equalTo(0));
        assertThat(pv.getValue(), equalTo((Object) Arrays.asList(2,3,4)));
    }

    @Test
    public void rateDecoupling() throws Exception {
        Queue<Integer> queue = queueOf(Integer.class).maxSize(10);
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
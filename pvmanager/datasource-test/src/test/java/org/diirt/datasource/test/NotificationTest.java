/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.datasource.test.ExpressionLanguage.*;
import org.diirt.datasource.test.TestDataSource;
import static java.time.Duration.*;

/**
 *
 * @author carcassi
 */
public class NotificationTest {

    @Test
    public void sequentialNotifications() throws Exception{
        CounterTestListener listener = new CounterTestListener();
        PVReader<Integer> reader = PVManager.read(counter())
                .readListener(listener)
                .from(new TestDataSource()).maxRate(ofMillis(10));
        Thread.sleep(100);
        if (listener.isFailed())
            fail("listener received wrong notifications");
        reader.close();
    }

    @Test
    public void pause() throws Exception{
        CounterTestListener listener = new CounterTestListener();
        PVReader<Integer> reader = PVManager.read(counter())
                .readListener(listener)
                .from(new TestDataSource()).maxRate(ofMillis(10));
        assertThat(reader.isPaused(), equalTo(false));
        Thread.sleep(100);

        // Pause
        reader.setPaused(true);
        assertThat(reader.isPaused(), equalTo(true));
        int currentCounter = listener.getNextExpected();
        Thread.sleep(100);
        assertThat("Notifications were sent when paused.", listener.getNextExpected(), lessThanOrEqualTo(currentCounter+1));

        // Resume
        reader.setPaused(false);
        assertThat(reader.isPaused(), equalTo(false));
        Thread.sleep(100);
        assertThat("Notifications were not resumed.", listener.getNextExpected(), not(equalTo(currentCounter)));
        if (listener.isFailed())
            fail("listener received wrong notifications");
        reader.close();
    }
}

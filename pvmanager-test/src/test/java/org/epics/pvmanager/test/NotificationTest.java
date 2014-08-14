/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.pvmanager.test.ExpressionLanguage.*;
import org.epics.pvmanager.test.TestDataSource;
import static org.epics.util.time.TimeDuration.*;

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

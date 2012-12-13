/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
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
        PVReader<Integer> reader = PVManager.read(counter()).from(new TestDataSource()).maxRate(ofMillis(10));
        CounterTestListener listener = new CounterTestListener();
        reader.addPVReaderListener(listener);
        Thread.sleep(100);
        if (listener.isFailed())
            fail("listener received wrong notifications");
        reader.close();
    }

    @Test
    public void pause() throws Exception{
        PVReader<Integer> reader = PVManager.read(counter()).from(new TestDataSource()).maxRate(ofMillis(10));
        CounterTestListener listener = new CounterTestListener();
        reader.addPVReaderListener(listener);
        assertThat(reader.isPaused(), equalTo(false));
        Thread.sleep(100);
        
        // Pause
        reader.setPaused(true);
        assertThat(reader.isPaused(), equalTo(true));
        int currentCounter = listener.getNextExpected();
        Thread.sleep(100);
        assertThat("Notifications were sent when paused.", listener.getNextExpected(), equalTo(currentCounter));
        
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

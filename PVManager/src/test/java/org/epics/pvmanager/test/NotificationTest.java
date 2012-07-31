/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.epics.pvmanager.PVWriterListener;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.ExpressionTester;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.ReadFailException;
import org.epics.pvmanager.TimeoutException;
import org.epics.pvmanager.WriteFailException;
import org.epics.pvmanager.util.TimeDuration;
import org.epics.pvmanager.util.TimeStamp;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.test.ExpressionLanguage.*;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.pvmanager.data.VInt;
import org.epics.pvmanager.data.VType;
import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.pvmanager.expression.DesiredRateExpression;
import static org.epics.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class NotificationTest {

    @Test
    public void sequentialNotifications() throws Exception{
        PVReader<VInt> reader = PVManager.read(counter()).from(new TestDataSource()).maxRate(ofMillis(10));
        CounterTestListener listener = new CounterTestListener(reader);
        reader.addPVReaderListener(listener);
        Thread.sleep(100);
        if (listener.isFailed())
            fail("listener received wrong notifications");
        reader.close();
    }
}

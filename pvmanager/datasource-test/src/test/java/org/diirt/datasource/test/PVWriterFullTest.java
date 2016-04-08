/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executor;

import org.diirt.datasource.DataSource;

import static org.diirt.datasource.ExpressionLanguage.*;

import org.diirt.datasource.PV;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVWriter;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.util.concurrent.Executors;

import static java.time.Duration.*;

import org.diirt.util.time.TimeInterval;

import static org.hamcrest.Matchers.*;

import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class PVWriterFullTest {

    public PVWriterFullTest() {
    }

    public static void waitForChannelToClose(DataSource source, String channelName) {
        Duration timeout = ofMillis(5000);
        TimeInterval timeoutInterval = TimeInterval.after(timeout, Instant.now());
        while (timeoutInterval.contains(Instant.now())) {
            if (source.getChannels().get(channelName) == null || !source.getChannels().get(channelName).isConnected()) {
                return;
            }
            try {
                Thread.sleep(100);
            } catch(Exception ex) {

            }
        }
        fail("Channel " + channelName + " didn't close after 5 seconds");
    }

    private DataSource dataSource;
    private Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor(Executors.namedPool("PVWriterFullTest "));

    PV<Object, Object> pv;
    PVReader<Object> pvReader;
    PVReader<Object> pvReader2;
    PVWriter<Object> pvWriter;

    @Before
    public void setupDataSource() throws Exception {
        dataSource = new TestDataSource();
    }

    @After
    public void closePVsAndDataSource() {
        if (pv != null) {
            pv.close();
            pv = null;
        }
        if (pvReader != null) {
            pvReader.close();
            pvReader = null;
        }
        if (pvReader2 != null) {
            pvReader2.close();
            pvReader2 = null;
        }
        if (pvWriter != null) {
            pvWriter.close();
            pvWriter = null;
        }

        waitForChannelToClose(dataSource, "delayedWrite");
        waitForChannelToClose(dataSource, "BrokenWrite");
        waitForChannelToClose(dataSource, "normal");
        waitForChannelToClose(dataSource, "delayedConnection");
        dataSource.close();
        dataSource = null;
    }

    @Test
    public void writerConnected() throws Exception {
        CountDownPVWriterListener<Object> listener = new CountDownPVWriterListener<>(1);
        pvWriter = PVManager.write(channel("normal"))
                .writeListener(listener)
                .notifyOn(executor)
                .from(dataSource)
                .async();

        // Wait for the connection notification
        listener.await(Duration.ofMillis(200));
        assertThat(listener.getCount(), equalTo(0));
        assertThat(listener.getEvent().getNotificationMask(), equalTo(PVWriterEvent.CONNECTION_MASK));
        assertThat(listener.getThreadName(), equalTo("PVWriterFullTest 1"));
        assertThat(pvWriter.lastWriteException(), equalTo(null));
        assertThat(pvWriter.isWriteConnected(), equalTo(true));
        assertThat(listener.getNotificationCount(), equalTo(1));
    }

    @Test
    public void writerWriteSuccessful() throws Exception {
        CountDownPVWriterListener<Object> listener = new CountDownPVWriterListener<>(1);
        pvWriter = PVManager.write(channel("normal"))
                .writeListener(listener)
                .notifyOn(executor)
                .from(dataSource)
                .async();

        // Wait for the connection notification
        listener.await(Duration.ofMillis(200));
        assertThat(listener.getCount(), equalTo(0));
        listener.resetCount(1);

        pvWriter.write("Value");
        listener.await(Duration.ofMillis(200));
        assertThat(listener.getCount(), equalTo(0));
        assertThat(listener.getEvent().getNotificationMask(), equalTo(PVWriterEvent.WRITE_SUCCEEDED_MASK));
        assertThat(listener.getThreadName(), equalTo("PVWriterFullTest 1"));
        assertThat(pvWriter.lastWriteException(), equalTo(null));
        assertThat(pvWriter.isWriteConnected(), equalTo(true));
        assertThat(listener.getNotificationCount(), equalTo(2));
    }

    @Test
    public void writerWriteFailed() throws Exception {
        CountDownPVWriterListener<Object> listener = new CountDownPVWriterListener<>(1);
        pvWriter = PVManager.write(channel("normal"))
                .writeListener(listener)
                .notifyOn(executor)
                .from(dataSource)
                .async();

        // Wait for the connection notification
        listener.await(Duration.ofMillis(200));
        assertThat(listener.getCount(), equalTo(0));
        listener.resetCount(1);

        pvWriter.write("Fail");
        listener.await(Duration.ofMillis(400));
        assertThat(listener.getCount(), equalTo(0));
        assertThat(listener.getEvent().getNotificationMask(), equalTo(PVWriterEvent.WRITE_FAILED_MASK));
        assertThat(listener.getThreadName(), equalTo("PVWriterFullTest 1"));
        Exception ex = pvWriter.lastWriteException();
        assertThat(ex, not(equalTo(null)));
        assertThat(ex.getMessage(), equalTo("Total failure"));
        assertThat(pvWriter.isWriteConnected(), equalTo(true));
        assertThat(listener.getNotificationCount(), equalTo(2));
    }

    @Test
    public void writerSyncWriteSuccessful() throws Exception {
        CountDownPVWriterListener<Object> listener = new CountDownPVWriterListener<>(1);
        pvWriter = PVManager.write(channel("normal"))
                .writeListener(listener)
                .notifyOn(executor)
                .from(dataSource)
                .sync();

        // Wait for the connection notification
        listener.await(Duration.ofMillis(200));
        assertThat(listener.getCount(), equalTo(0));

        pvWriter.write("Value");

        assertThat(listener.getEvent().getNotificationMask(), equalTo(PVWriterEvent.WRITE_SUCCEEDED_MASK));
        assertThat(listener.getThreadName(), equalTo("PVWriterFullTest 1"));
        assertThat(pvWriter.lastWriteException(), equalTo(null));
        assertThat(pvWriter.isWriteConnected(), equalTo(true));
        assertThat(listener.getNotificationCount(), equalTo(2));
    }

    @Test
    public void writerSyncWriteFailed() throws Exception {
        CountDownPVWriterListener<Object> listener = new CountDownPVWriterListener<>(1);
        pvWriter = PVManager.write(channel("normal"))
                .writeListener(listener)
                .notifyOn(executor)
                .from(dataSource)
                .sync();

        // Wait for the connection notification
        listener.await(Duration.ofMillis(200));
        assertThat(listener.getCount(), equalTo(0));

        Throwable cause = null;
        try {
            pvWriter.write("Fail");
        } catch(Exception ex) {
            assertThat(ex, instanceOf(RuntimeException.class));
            assertThat(ex.getMessage(), equalTo("Write failed"));
            cause = ex.getCause();
        }

        assertThat(listener.getEvent().getNotificationMask(), equalTo(PVWriterEvent.WRITE_FAILED_MASK));
        assertThat(listener.getThreadName(), equalTo("PVWriterFullTest 1"));
        Exception ex = pvWriter.lastWriteException();
        assertThat(ex, not(equalTo(null)));
        assertThat(ex.getMessage(), equalTo("Total failure"));
        assertThat(ex, sameInstance(cause));
        assertThat(pvWriter.isWriteConnected(), equalTo(true));
        assertThat(listener.getNotificationCount(), equalTo(2));
    }

    @Test
    public void writerConnectionTimeout() {
        // create writer with timeout and delayed connection
        // wait for notification
        // check elapsed time
        // check notification type (correct exception)
        // check thread for notification
        // check writer state
        // wait for second notification
        // check notification type (connection)
        // check thread for notification
        // check writer state
    }

}

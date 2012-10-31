/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.epics.pvmanager.CountDownPVReaderListener;
import org.epics.pvmanager.CountDownPVWriterListener;
import org.epics.pvmanager.PVWriterListener;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.ReadFailException;
import org.epics.pvmanager.TimeoutException;
import org.epics.pvmanager.WriteFailException;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.MockPVWriteListener;
import static org.epics.util.time.TimeDuration.*;
import org.epics.util.time.TimeInterval;
import static org.epics.pvmanager.ThreadTestingUtil.*;
import org.junit.After;

/**
 *
 * @author carcassi
 */
public class TestDataSourceTest {
    
    public TestDataSourceTest() {
    }
    
    public static void waitForChannelToClose(DataSource source, String channelName) {
        TimeDuration timeout = ofMillis(5000);
        TimeInterval timeoutInterval = timeout.after(Timestamp.now());
        while (timeoutInterval.contains(Timestamp.now())) {
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

    @BeforeClass
    public static void setUpClass() throws Exception {
        dataSource = new TestDataSource();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        dataSource.close();
        dataSource = null;
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    private static DataSource dataSource;
    @Mock PVReaderListener<Object> readListener;
    
    PVReader<Object> pvReader;
    PVWriter<Object> pvWriter;
    
    @After
    public void closePVs() {
        if (pvReader != null) {
            pvReader.close();
        }
        
        if (pvWriter != null) {
            pvWriter.close();
        }

        waitForChannelToClose(dataSource, "delayedWrite");
    }
    
    @Test
    public void channelDoesNotExist1() throws Exception {
        // Requesting a channel that does not exist
        // Making sure that the exception is properly notified
        CountDownPVReaderListener listener = new CountDownPVReaderListener(1);
        pvReader = PVManager.read(channel("nothing"))
                .readListener(listener)
                .from(dataSource).maxRate(ofMillis(10));
        
        listener.await(TimeDuration.ofMillis(100));
        
        ReadFailException ex = (ReadFailException) pvReader.lastException();
        assertThat(ex, not(nullValue()));
    }
    
    @Test
    public void channelDoesNotExist2() throws Exception {
        // Requesting a channel that does not exist
        // Making sure that the excecption is properly notified
        CountDownPVWriterListener listener = new CountDownPVWriterListener(1);
        pvWriter = PVManager.write(channel("nothing"))
                .listeners(listener)
                .from(dataSource).async();

        listener.await(TimeDuration.ofMillis(100));
        
        WriteFailException ex = (WriteFailException) pvWriter.lastWriteException();
        assertThat(ex, not(nullValue()));
    }
    
    @Test
    public void delayedWrite() throws Exception {
        PVWriter<Object> pvWriter = PVManager.write(channel("delayedWrite")).from(dataSource).async();
        MockPVWriteListener<Object> writeListener = MockPVWriteListener.addPVWriteListener(pvWriter);
        pvWriter.write("test");
        
        Thread.sleep(15);
        
        WriteFailException ex = (WriteFailException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        assertThat(writeListener.getCounter(), equalTo(0));
        
        Thread.sleep(1100);
        
        ex = (WriteFailException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        assertThat(writeListener.getCounter(), equalTo(1));
        
        pvWriter.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedWrite");
    }
    
    @Test
    public void delayedWriteWithTimeout() throws Exception {
        final PVWriter<Object> pvWriter = PVManager.write(channel("delayedWrite")).timeout(ofMillis(500)).from(dataSource).async();
        MockPVWriteListener<Object> writeListener = MockPVWriteListener.addPVWriteListener(pvWriter);
        pvWriter.write("test");
        
        Callable<TimeoutException> getWriteException = new Callable<TimeoutException>() {

            @Override
            public TimeoutException call() throws Exception {
                return (TimeoutException) pvWriter.lastWriteException();
            }
        };
        
        TimeoutException ex = waitFor(getWriteException, TimeDuration.ofMillis(600)); 
        assertThat(ex, not(nullValue()));
        assertThat(writeListener.getCounter(), equalTo(1));
        
        ex = waitFor(getWriteException, TimeDuration.ofMillis(2000)); 
        assertThat(ex, nullValue());
        assertThat(writeListener.getCounter(), equalTo(2));
        
        pvWriter.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedWrite");
    }
    
    @Test
    public void delayedWriteWithTimeout2() throws Exception {
        // Test a write that happens 2 seconds late
        // Checks whether we get a timeout beforehand
        pvWriter = PVManager.write(channel("delayedWrite")).timeout(ofMillis(500)).from(dataSource).async();
        CountDownPVWriterListener writerListener = new CountDownPVWriterListener(1);
        pvWriter.addPVWriterListener(writerListener);
        pvWriter.write("test");

        // Wait for the first notification, should be the timeout
        writerListener.await(TimeDuration.ofMillis(600));
        assertThat(writerListener.getCount(), equalTo(0));
        writerListener.resetCount(1);
        
        Exception ex = pvWriter.lastWriteException(); 
        assertThat(ex, not(nullValue()));
        assertThat(ex, instanceOf(TimeoutException.class));

        // Wait for the second notification, should be
        // the success notification
        writerListener.await(TimeDuration.ofMillis(2000));
        assertThat(writerListener.getCount(), equalTo(0));
        writerListener.resetCount(1);
        
        ex = pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        
        // Write again
        pvWriter.write("test2");
        
        // Wait for a notification: should not come
        writerListener.await(TimeDuration.ofMillis(400));
        assertThat(writerListener.getCount(), equalTo(1));
        ex = pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        
        writerListener.await(TimeDuration.ofMillis(200));
        assertThat(writerListener.getCount(), equalTo(0));
        writerListener.resetCount(1);
        ex = pvWriter.lastWriteException();
        assertThat(ex, not(nullValue()));
        
        writerListener.await(TimeDuration.ofMillis(2000));
        assertThat(writerListener.getCount(), equalTo(0));
        ex = pvWriter.lastWriteException();
        assertThat(ex, nullValue());
    }
    
    @Test
    public void delayedReadConnectionWithTimeout() throws Exception {
        PVReader<Object> pvReader = PVManager.read(channel("delayedConnection")).timeout(ofMillis(500)).from(dataSource).maxRate(ofMillis(50));
        pvReader.addPVReaderListener(readListener);
        
        Thread.sleep(50);
        
        TimeoutException ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged(pvReader);
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, not(nullValue()));
        verify(readListener).pvChanged(pvReader);
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, nullValue());
        verify(readListener, times(2)).pvChanged(pvReader);
        assertThat((String) pvReader.getValue(), equalTo("Initial value"));
        
        pvReader.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedConnection");
    }
    
    @Test
    public void delayedReadOnPVWithTimeout() throws Exception {
        PV<Object, Object> pv = PVManager.readAndWrite(channel("delayedConnection")).timeout(ofMillis(500)).from(dataSource).asynchWriteAndMaxReadRate(ofMillis(50));
        pv.addPVReaderListener(readListener);
        
        Thread.sleep(50);
        
        TimeoutException ex = (TimeoutException) pv.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged(pv);
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pv.lastException();
        assertThat(ex, not(nullValue()));
        verify(readListener).pvChanged(pv);
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pv.lastException();
        assertThat(ex, nullValue());
        verify(readListener, times(2)).pvChanged(pv);
        assertThat((String) pv.getValue(), equalTo("Initial value"));
        
        pv.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedConnection");
    }
    
    @Test
    public void delayedReadOnPVWithTimeoutAndCustomMessage() throws Exception {
        String message = "Ouch! Timeout!";
        PV<Object, Object> pv = PVManager.readAndWrite(channel("delayedConnection")).timeout(ofMillis(500), message).from(dataSource).asynchWriteAndMaxReadRate(ofMillis(50));
        pv.addPVReaderListener(readListener);
        
        Thread.sleep(50);
        
        TimeoutException ex = (TimeoutException) pv.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged(pv);
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pv.lastException();
        assertThat(ex, not(nullValue()));
        assertThat(ex.getMessage(), equalTo(message));
        verify(readListener).pvChanged(pv);
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pv.lastException();
        assertThat(ex, nullValue());
        verify(readListener, times(2)).pvChanged(pv);
        assertThat((String) pv.getValue(), equalTo("Initial value"));
        
        pv.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedConnection");
    }
    
    @Test
    public void delayedMultipleReadWithConnectionError() throws Exception {
        PVReader<Object> pv1 = PVManager.read(channel("delayedConnectionError")).from(dataSource).maxRate(ofMillis(50));
        pv1.addPVReaderListener(readListener);
        PVReader<Object> pv2 = PVManager.read(channel("delayedConnectionError")).from(dataSource).maxRate(ofMillis(50));
        pv2.addPVReaderListener(readListener);
        
        Thread.sleep(50);
        
        RuntimeException ex = (RuntimeException) pv1.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged(pv1);
        ex = (RuntimeException) pv2.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged(pv1);
        
        Thread.sleep(1100);
        
        ex = (RuntimeException) pv1.lastException();
        assertThat(ex, instanceOf(RuntimeException.class));
        assertThat(ex.getMessage(), equalTo("Connection error"));
        ex = (RuntimeException) pv2.lastException();
        assertThat(ex, instanceOf(RuntimeException.class));
        assertThat(ex.getMessage(), equalTo("Connection error"));
        verify(readListener, times(1)).pvChanged(pv1);
        verify(readListener, times(1)).pvChanged(pv2);
        
        pv1.close();
        pv2.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedConnection");
    }
}

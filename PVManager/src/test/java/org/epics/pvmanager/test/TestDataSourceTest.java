/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.test;

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
import org.epics.pvmanager.util.TimeDuration;
import org.epics.pvmanager.util.TimeStamp;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.util.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class TestDataSourceTest {
    
    public TestDataSourceTest() {
    }
    
    public static void waitForChannelToClose(DataSource source, String channelName) {
        TimeStamp startTime = TimeStamp.now();
        TimeDuration timeout = ms(5000);
        while (TimeStamp.now().durationFrom(startTime).getNanoSec() < timeout.getNanoSec()) {
            if (!source.getChannels().get(channelName).isConnected()) {
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
        dataSource = null;
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    private static DataSource dataSource;
    @Mock PVWriterListener writeListener;
    @Mock PVReaderListener readListener;
    
    @Test
    public void channelDoesNotExist1() throws Exception {
        PVReader<Object> pvReader = PVManager.read(channel("nothing")).from(dataSource).every(ms(10));
        pvReader.addPVReaderListener(readListener);
        
        Thread.sleep(50);
        
        ReadFailException ex = (ReadFailException) pvReader.lastException();
        assertThat(ex, not(nullValue()));
        verify(readListener).pvChanged();
        pvReader.close();
    }
    
    @Test
    public void channelDoesNotExist2() throws Exception {
        PVWriter<Object> pvWriter = PVManager.write(channel("nothing")).from(dataSource).async();
        pvWriter.addPVWriterListener(writeListener);
        
        Thread.sleep(15);
        
        WriteFailException ex = (WriteFailException) pvWriter.lastWriteException();
        assertThat(ex, not(nullValue()));
        verify(writeListener).pvWritten();
        pvWriter.close();
    }
    
    @Test
    public void delayedWrite() throws Exception {
        PVWriter<Object> pvWriter = PVManager.write(channel("delayedWrite")).from(dataSource).async();
        pvWriter.addPVWriterListener(writeListener);
        pvWriter.write("test");
        
        Thread.sleep(15);
        
        WriteFailException ex = (WriteFailException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener, never()).pvWritten();
        
        Thread.sleep(1100);
        
        ex = (WriteFailException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener).pvWritten();
        
        pvWriter.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedWrite");
    }
    
    @Test
    public void delayedWriteWithTimeout() throws Exception {
        PVWriter<Object> pvWriter = PVManager.write(channel("delayedWrite")).timeout(ms(500)).from(dataSource).async();
        pvWriter.addPVWriterListener(writeListener);
        pvWriter.write("test");
        
        Thread.sleep(15);
        
        TimeoutException ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener, never()).pvWritten();
        
        Thread.sleep(500);
        
        ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, not(nullValue()));
        verify(writeListener).pvWritten();
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener, times(2)).pvWritten();
        
        pvWriter.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedWrite");
    }
    
    @Test
    public void delayedWriteWithTimeout2() throws Exception {
        PVWriter<Object> pvWriter = PVManager.write(channel("delayedWrite")).timeout(ms(500)).from(dataSource).async();
        pvWriter.addPVWriterListener(writeListener);
        pvWriter.write("test");
        
        Thread.sleep(15);
        
        TimeoutException ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener, never()).pvWritten();
        
        Thread.sleep(500);
        
        ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, not(nullValue()));
        verify(writeListener).pvWritten();
        
        Thread.sleep(500);
        
        ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener, times(2)).pvWritten();
        
        // Write again
        pvWriter.write("test2");
        
        Thread.sleep(15);
        
        ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener, times(2)).pvWritten();
        
        Thread.sleep(500);
        
        ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, not(nullValue()));
        verify(writeListener, times(3)).pvWritten();
        
        Thread.sleep(500);
        
        ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener, times(4)).pvWritten();
        
        pvWriter.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedWrite");
    }
    
    @Test
    public void delayedReadConnectionWithTimeout() throws Exception {
        PVReader<Object> pvReader = PVManager.read(channel("delayedConnection")).timeout(ms(500)).from(dataSource).every(ms(50));
        pvReader.addPVReaderListener(readListener);
        
        Thread.sleep(15);
        
        TimeoutException ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged();
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, not(nullValue()));
        verify(readListener).pvChanged();
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, nullValue());
        verify(readListener, times(2)).pvChanged();
        assertThat((String) pvReader.getValue(), equalTo("Initial value"));
        
        pvReader.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedConnection");
    }
    
    @Test
    public void delayedReadOnPVWithTimeout() throws Exception {
        PV<Object, Object> pv = PVManager.readAndWrite(channel("delayedConnection")).timeout(ms(500)).from(dataSource).asynchWriteAndReadEvery(ms(50));
        pv.addPVReaderListener(readListener);
        
        Thread.sleep(50);
        
        TimeoutException ex = (TimeoutException) pv.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged();
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pv.lastException();
        assertThat(ex, not(nullValue()));
        verify(readListener).pvChanged();
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pv.lastException();
        assertThat(ex, nullValue());
        verify(readListener, times(2)).pvChanged();
        assertThat((String) pv.getValue(), equalTo("Initial value"));
        
        pv.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedConnection");
    }
    
    @Test
    public void delayedReadOnPVWithTimeoutAndCustomMessage() throws Exception {
        String message = "Ouch! Timeout!";
        PV<Object, Object> pv = PVManager.readAndWrite(channel("delayedConnection")).timeout(ms(500), message).from(dataSource).asynchWriteAndReadEvery(ms(50));
        pv.addPVReaderListener(readListener);
        
        Thread.sleep(50);
        
        TimeoutException ex = (TimeoutException) pv.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged();
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pv.lastException();
        assertThat(ex, not(nullValue()));
        assertThat(ex.getMessage(), equalTo(message));
        verify(readListener).pvChanged();
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pv.lastException();
        assertThat(ex, nullValue());
        verify(readListener, times(2)).pvChanged();
        assertThat((String) pv.getValue(), equalTo("Initial value"));
        
        pv.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedConnection");
    }
    
    @Test
    public void delayedMultipleReadWithConnectionError() throws Exception {
        PVReader<Object> pv1 = PVManager.read(channel("delayedConnectionError")).from(dataSource).every(ms(50));
        pv1.addPVReaderListener(readListener);
        PVReader<Object> pv2 = PVManager.read(channel("delayedConnectionError")).from(dataSource).every(ms(50));
        pv2.addPVReaderListener(readListener);
        
        Thread.sleep(50);
        
        RuntimeException ex = (RuntimeException) pv1.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged();
        ex = (RuntimeException) pv2.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged();
        
        Thread.sleep(1100);
        
        ex = (RuntimeException) pv1.lastException();
        assertThat(ex, instanceOf(RuntimeException.class));
        assertThat(ex.getMessage(), equalTo("Connection error"));
        ex = (RuntimeException) pv2.lastException();
        assertThat(ex, instanceOf(RuntimeException.class));
        assertThat(ex.getMessage(), equalTo("Connection error"));
        verify(readListener, times(2)).pvChanged();
        
        pv1.close();
        pv2.close();
        Thread.sleep(30);
        waitForChannelToClose(dataSource, "delayedConnection");
    }
}

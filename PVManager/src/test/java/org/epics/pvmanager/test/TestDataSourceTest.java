/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.PVWriterListener;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.ReadFailException;
import org.epics.pvmanager.TimeoutException;
import org.epics.pvmanager.WriteFailException;
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
        
        Thread.sleep(15);
        
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
        
        Thread.sleep(1000);
        
        ex = (WriteFailException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener).pvWritten();
        
        pvWriter.close();
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
        
        Thread.sleep(500);
        
        ex = (TimeoutException) pvWriter.lastWriteException();
        assertThat(ex, nullValue());
        verify(writeListener, times(2)).pvWritten();
        
        pvWriter.close();
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
    }
    
    @Test
    public void delayedReadConnectionWithTimeout() throws Exception {
        PVReader<Object> pvReader = PVManager.read(channel("delayedConnection")).timeout(ms(500)).from(dataSource).every(ms(50));
        pvReader.addPVReaderListener(readListener);
        
        Thread.sleep(15);
        
        TimeoutException ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, nullValue());
        verify(readListener, never()).pvChanged();
        
        Thread.sleep(500);
        
        ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, not(nullValue()));
        verify(readListener).pvChanged();
        
        Thread.sleep(600);
        
        ex = (TimeoutException) pvReader.lastException();
        assertThat(ex, nullValue());
        verify(readListener, times(2)).pvChanged();
        assertThat((String) pvReader.getValue(), equalTo("Initial value"));
        
        pvReader.close();
    }
}

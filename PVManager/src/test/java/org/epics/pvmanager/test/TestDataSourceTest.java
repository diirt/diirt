/*
 * Copyright 2011 Brookhaven National Laboratory
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
}

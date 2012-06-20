/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.DataSourceTypeAdapter;
import org.epics.pvmanager.DataSourceTypeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.epics.pvmanager.ValueCache;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.expression.DesiredRateReadWriteExpression;
import org.mockito.Mockito;
import org.mockito.Spy;

/**
 *
 * @author carcassi
 */
public class DatasourceTest {

    public DatasourceTest() {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    private class MockDataSource extends DataSource {

        public MockDataSource(boolean writeable) {
            super(writeable);
        }

        @Override
        protected ChannelHandler createChannel(String channelName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    };
    
    @Mock ChannelHandler channel1;
    @Mock ChannelHandler channel2;

    @Test
    public void connect1() {
        // A simple recipe with one channel
        
        DataRecipe dataRecipe = latestValueOf(channel("first")).getDataRecipe();
        
        DataSource dataSource = spy(new MockDataSource(true));
        
        doReturn(channel1).when(dataSource).createChannel("first");
        
        dataSource.connect(dataRecipe);
        
        verify(dataSource).channel("first");
        verify(channel1).addMonitor(dataRecipe.getChannelsPerCollectors().keySet().iterator().next(), 
                dataRecipe.getChannelsPerCollectors().values().iterator().next().values().iterator().next(), dataRecipe.getExceptionHandler());
    }

    @Test
    public void connect2() {
        // A recipe with two channels
        
        DesiredRateReadWriteExpression<Map<String, Object>, Map<String, Object>> exp = mapOf(latestValueOf(channel("first").and(channel("second"))));
        DataRecipe dataRecipe = exp.getDataRecipe();
        ExpressionTester expHelper = new ExpressionTester(exp);
        
        DataSource dataSource = spy(new MockDataSource(true));
        
        doReturn(channel1).when(dataSource).createChannel("first");
        doReturn(channel2).when(dataSource).createChannel("second");
        
        dataSource.connect(dataRecipe);
        
        verify(dataSource).channel("first");
        verify(dataSource).channel("second");
        verify(channel1).addMonitor(expHelper.collectorFor("first"), 
                expHelper.cacheFor("first"), dataRecipe.getExceptionHandler());
        verify(channel2).addMonitor(expHelper.collectorFor("second"), 
                expHelper.cacheFor("second"), dataRecipe.getExceptionHandler());
    }

    @Test
    public void connect3() {
        // Two recipe with the same channel: create only one
        
        DataRecipe firstRecipe = latestValueOf(channel("first")).getDataRecipe();
        
        DataSource dataSource = spy(new MockDataSource(true));
        
        doReturn(channel1).when(dataSource).createChannel("first");
        
        dataSource.connect(firstRecipe);
        
        DataRecipe secondRecipe = latestValueOf(channel("first")).getDataRecipe();
        
        dataSource.connect(secondRecipe);
        
        verify(dataSource, times(2)).channel("first");
        verify(dataSource).createChannel("first");
        verify(channel1).addMonitor(firstRecipe.getChannelsPerCollectors().keySet().iterator().next(), 
                firstRecipe.getChannelsPerCollectors().values().iterator().next().values().iterator().next(), firstRecipe.getExceptionHandler());
        verify(channel1).addMonitor(secondRecipe.getChannelsPerCollectors().keySet().iterator().next(), 
                secondRecipe.getChannelsPerCollectors().values().iterator().next().values().iterator().next(), secondRecipe.getExceptionHandler());
    }
}

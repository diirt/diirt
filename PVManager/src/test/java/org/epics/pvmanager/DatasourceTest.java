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
import org.epics.pvmanager.expression.DesiredRateExpression;
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
        protected String channelHandlerLookupName(String channelName) {
            if ("changeit".equals(channelName))
                return "first";
            return channelName;
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
        
        ExpressionTester exp = new ExpressionTester(latestValueOf(channel("first")));
        DataRecipe dataRecipe = exp.getDataRecipe();
        
        DataSource dataSource = spy(new MockDataSource(true));
        doReturn(channel1).when(dataSource).createChannel("first");
        
        dataSource.connect(dataRecipe);
        
        verify(dataSource).channel("first");
        verify(channel1).addMonitor(exp.recipeFor("first").getReadSubscription());
    }

    @Test
    public void connect2() {
        // A recipe with two channels
        
        ExpressionTester exp = new ExpressionTester(mapOf(latestValueOf(channel("first").and(channel("second")))));
        DataRecipe dataRecipe = exp.getDataRecipe();
        
        DataSource dataSource = spy(new MockDataSource(true));
        doReturn(channel1).when(dataSource).createChannel("first");
        doReturn(channel2).when(dataSource).createChannel("second");
        
        dataSource.connect(dataRecipe);
        
        verify(dataSource).channel("first");
        verify(dataSource).channel("second");
        verify(channel1).addMonitor(exp.recipeFor("first").getReadSubscription());
        verify(channel2).addMonitor(exp.recipeFor("second").getReadSubscription());
    }

    @Test
    public void connect3() {
        // Two recipe with the same channel: create only one
        
        ExpressionTester exp1 = new ExpressionTester(latestValueOf(channel("first")));
        ExpressionTester exp2 = new ExpressionTester(latestValueOf(channel("first")));
        DataRecipe dataRecipe1 = exp1.getDataRecipe();
        DataRecipe dataRecipe2 = exp2.getDataRecipe();
        
        DataSource dataSource = spy(new MockDataSource(true));
        doReturn(channel1).when(dataSource).createChannel("first");
        
        dataSource.connect(dataRecipe1);
        dataSource.connect(dataRecipe2);
        
        verify(dataSource, times(2)).channel("first");
        verify(dataSource).createChannel("first");
        verify(channel1).addMonitor(exp1.recipeFor("first").getReadSubscription());
        verify(channel1).addMonitor(exp2.recipeFor("first").getReadSubscription());
    }

    @Test
    public void connect4() {
        // A simple recipe with one channel
        
        ExpressionTester exp = new ExpressionTester(latestValueOf(channel("changeit")));
        DataRecipe dataRecipe = exp.getDataRecipe();
        
        DataSource dataSource = spy(new MockDataSource(true));
        doReturn(channel1).when(dataSource).createChannel("changeit");
        
        dataSource.connect(dataRecipe);
        
        verify(dataSource).channel("changeit");
        verify(dataSource).createChannel("changeit");
        verify(channel1).addMonitor(exp.recipeFor("changeit").getReadSubscription());
        assertThat(dataSource.getChannels().get("first"), not(equalTo(null)));
        assertThat(dataSource.getChannels().get("changeit"), equalTo(null));
    }
}

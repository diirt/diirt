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
import org.epics.pvmanager.ValueCache;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
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

    @Test
    public void find1() {
        DataRecipe dataRecipe = latestValueOf(channel("first")).getDataRecipe();
        
        DataSource dataSource = spy(new MockDataSource(true));
        
        doReturn(channel1).when(dataSource).createChannel("first");
        
        dataSource.connect(dataRecipe);
        
        verify(dataSource).channel("first");
        verify(channel1).addMonitor(dataRecipe.getChannelsPerCollectors().keySet().iterator().next(), 
                dataRecipe.getChannelsPerCollectors().values().iterator().next().values().iterator().next(), dataRecipe.getExceptionHandler());
    }
}

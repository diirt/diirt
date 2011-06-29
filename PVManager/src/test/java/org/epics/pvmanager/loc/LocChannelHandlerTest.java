/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.loc;

import org.epics.pvmanager.Collector;
import org.epics.pvmanager.TUtil;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.VDouble;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author carcassi
 */
public class LocChannelHandlerTest {

    public LocChannelHandlerTest() {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Spy ValueCache<VDouble> vDoubleCache = new ValueCache<VDouble>(VDouble.class);
    @Mock Collector<VDouble> vDoubleCollector;
    @Mock ChannelWriteCallback failOnException;

    @Test
    public void testChannelHandler() {
        
        // Creating a test local channel
        LocChannelHandler channel = new LocChannelHandler("test1");
        assertThat(channel.getChannelName(), equalTo("test1"));
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));

        // Attaching a monitor cache/collactor
        channel.addMonitor(vDoubleCollector, vDoubleCache, TUtil.failOnException());
        assertThat(channel.getUsageCounter(), equalTo(1));
        assertThat(channel.isConnected(), is(true));

        // Adding a writer
        channel.addWriter(TUtil.failOnException());
        assertThat(channel.getUsageCounter(), equalTo(2));
        assertThat(channel.isConnected(), is(true));

        // Writing a number and see if it is converted to a VDouble
        channel.write(6.28, failOnException);
        
        InOrder inOrder = inOrder(vDoubleCache, vDoubleCollector, failOnException);
        ArgumentCaptor<VDouble> newValue = ArgumentCaptor.forClass(VDouble.class); 
        inOrder.verify(vDoubleCache).setValue(newValue.capture());
        assertThat(newValue.getValue().getValue(), equalTo(6.28));
        inOrder.verify(vDoubleCollector).collect();
        inOrder.verify(failOnException).channelWritten(null);
    }
}

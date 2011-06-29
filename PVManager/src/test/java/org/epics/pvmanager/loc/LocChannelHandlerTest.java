/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.loc;

import org.epics.pvmanager.Collector;
import org.epics.pvmanager.ExceptionHandler;
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
    
    @Mock ValueCache<VDouble> vDoubleCache1;
    @Mock Collector<VDouble> vDoubleCollector1;
    @Mock ValueCache<VDouble> vDoubleCache2;
    @Mock Collector<VDouble> vDoubleCollector2;
    @Mock ChannelWriteCallback failOnException;
    @Mock ExceptionHandler exceptionHandler;

    @Test
    public void writeToLocalChannelSingleMonitor() {
        
        // Creating a test local channel
        LocChannelHandler channel = new LocChannelHandler("test1");
        assertThat(channel.getChannelName(), equalTo("test1"));
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));

        // Attaching a monitor cache/collector
        channel.addMonitor(vDoubleCollector1, vDoubleCache1, exceptionHandler);
        assertThat(channel.getUsageCounter(), equalTo(1));
        assertThat(channel.isConnected(), is(true));

        // Adding a writer
        channel.addWriter(exceptionHandler);
        assertThat(channel.getUsageCounter(), equalTo(2));
        assertThat(channel.isConnected(), is(true));

        // Writing a number and see if it is converted to a VDouble
        channel.write(6.28, failOnException);
        
        InOrder inOrder = inOrder(vDoubleCache1, vDoubleCollector1, failOnException);
        ArgumentCaptor<VDouble> newValue = ArgumentCaptor.forClass(VDouble.class); 
        inOrder.verify(vDoubleCache1).setValue(newValue.capture());
        assertThat(newValue.getValue().getValue(), equalTo(6.28));
        inOrder.verify(vDoubleCollector1).collect();
        inOrder.verify(failOnException).channelWritten(null);
        verifyZeroInteractions(exceptionHandler);
    }

    @Test
    public void writeToLocalChannelTwoMonitors() {
        
        // Creating a test local channel
        LocChannelHandler channel = new LocChannelHandler("test2");
        assertThat(channel.getChannelName(), equalTo("test2"));
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));

        // Attaching a monitor cache/collector
        channel.addMonitor(vDoubleCollector1, vDoubleCache1, exceptionHandler);
        assertThat(channel.getUsageCounter(), equalTo(1));
        assertThat(channel.isConnected(), is(true));

        // Attaching a monitor cache/collector
        channel.addMonitor(vDoubleCollector2, vDoubleCache2, exceptionHandler);
        assertThat(channel.getUsageCounter(), equalTo(2));
        assertThat(channel.isConnected(), is(true));

        // Adding a writer
        channel.addWriter(exceptionHandler);
        assertThat(channel.getUsageCounter(), equalTo(3));
        assertThat(channel.isConnected(), is(true));

        // Writing a number and see if it is converted to a VDouble
        channel.write(16.28, failOnException);
        
        InOrder inOrder = inOrder(vDoubleCache1, vDoubleCollector1, vDoubleCache2, vDoubleCollector2, failOnException);
        ArgumentCaptor<VDouble> newValue = ArgumentCaptor.forClass(VDouble.class); 
        inOrder.verify(vDoubleCache1).setValue(newValue.capture());
        assertThat(newValue.getValue().getValue(), equalTo(16.28));
        inOrder.verify(vDoubleCollector1).collect();
        inOrder.verify(vDoubleCache2).setValue(newValue.capture());
        assertThat(newValue.getValue().getValue(), equalTo(16.28));
        inOrder.verify(vDoubleCollector2).collect();
        inOrder.verify(failOnException).channelWritten(null);
        verifyZeroInteractions(exceptionHandler);
    }
}

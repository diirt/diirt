/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.loc;

import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.VDouble;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    @Mock ChannelWriteCallback channelWriteCallback;
    @Mock ExceptionHandler exceptionHandler;

    @Test
    public void writeToLocalChannelSingleMonitor() {
        
        // Creating a test local channel
        LocalChannelHandler channel = new LocalChannelHandler("test1");
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
        channel.write(6.28, channelWriteCallback);
        
        // Removing all readers and writers
        channel.removeMonitor(vDoubleCollector1);
        channel.removeWrite(exceptionHandler);
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));
        
        InOrder inOrder = inOrder(vDoubleCache1, vDoubleCollector1, channelWriteCallback, exceptionHandler);
        ArgumentCaptor<VDouble> newValue = ArgumentCaptor.forClass(VDouble.class); 
        inOrder.verify(vDoubleCache1).setValue(newValue.capture());
        assertThat(newValue.getValue().getValue(), equalTo(6.28));
        inOrder.verify(vDoubleCollector1).collect();
        inOrder.verify(channelWriteCallback).channelWritten(null);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void writeToLocalChannelTwoMonitors() {
        
        // Creating a test local channel
        LocalChannelHandler channel = new LocalChannelHandler("test2", 0.0);
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
        channel.write(16.28, channelWriteCallback);
        
        // Remove reader/writers
        channel.removeWrite(exceptionHandler);
        channel.removeMonitor(vDoubleCollector1);
        channel.removeMonitor(vDoubleCollector2);
        assertThat(channel.getUsageCounter(), equalTo(0));
        assertThat(channel.isConnected(), is(false));
        
        ArgumentCaptor<VDouble> newValue = ArgumentCaptor.forClass(VDouble.class); 
        verify(vDoubleCache1, times(2)).setValue(newValue.capture());
        assertThat(newValue.getAllValues().get(0).getValue(), equalTo(0.0));
        assertThat(newValue.getAllValues().get(1).getValue(), equalTo(16.28));
        verify(vDoubleCollector1, times(2)).collect();
        ArgumentCaptor<VDouble> newValue2 = ArgumentCaptor.forClass(VDouble.class); 
        verify(vDoubleCache2, times(2)).setValue(newValue2.capture());
        assertThat(newValue2.getAllValues().get(0).getValue(), equalTo(0.0));
        assertThat(newValue2.getAllValues().get(1).getValue(), equalTo(16.28));
        verify(vDoubleCollector2, times(2)).collect();
        verify(channelWriteCallback).channelWritten(null);
        verifyZeroInteractions(channelWriteCallback, exceptionHandler);
    }
}

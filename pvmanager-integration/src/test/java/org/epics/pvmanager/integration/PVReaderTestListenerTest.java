/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import org.epics.pvmanager.DataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.TimeoutException;
import org.epics.util.time.TimeDuration;
import static org.epics.util.time.TimeDuration.*;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

/**
 *
 * @author carcassi
 */
public class PVReaderTestListenerTest {
    
    public static <T> PVReaderEvent<T> createDisconnectedEvent() {
        @SuppressWarnings("unchecked")
        PVReader<T> disconnectedPvReader = (PVReader<T>) mock(PVReader.class);
        when(disconnectedPvReader.isConnected()).thenReturn(false);
        
        @SuppressWarnings("unchecked")
        PVReaderEvent<T> disconnectedEvent = (PVReaderEvent<T>) mock(PVReaderEvent.class);
        when(disconnectedEvent.isConnectionChanged()).thenReturn(true);
        when(disconnectedEvent.getPvReader()).thenReturn(disconnectedPvReader);
        
        return disconnectedEvent;
    }
    
    public static <T> PVReaderEvent<T> createConnectedEvent() {
        @SuppressWarnings("unchecked")
        PVReader<T> connectedPvReader = (PVReader<T>) mock(PVReader.class);
        when(connectedPvReader.isConnected()).thenReturn(true);
        
        @SuppressWarnings("unchecked")
        PVReaderEvent<T> connectedEvent = (PVReaderEvent<T>) mock(PVReaderEvent.class);
        when(connectedEvent.isConnectionChanged()).thenReturn(true);
        when(connectedEvent.getPvReader()).thenReturn(connectedPvReader);
        
        return connectedEvent;
    }
    
    @Test
    public void matchConnections1() throws Exception {
        PVReaderTestListener<Object> listener = PVReaderTestListener.matchConnections(true, false, true);
        listener.pvChanged(createConnectedEvent());
        listener.pvChanged(createDisconnectedEvent());
        listener.pvChanged(createConnectedEvent());
        
        listener.close();
        assertThat(listener.getErrorMessage(), nullValue());
        assertThat(listener.isSuccess(), equalTo(true));
    }
}

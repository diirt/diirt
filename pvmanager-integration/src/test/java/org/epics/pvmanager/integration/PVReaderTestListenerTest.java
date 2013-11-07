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
import org.epics.vtype.VDouble;
import org.epics.vtype.VString;
import org.epics.vtype.ValueFactory;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.epics.vtype.ValueFactory.*;

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
    
    public static <T, V extends T> PVReaderEvent<T> createValueEvent(V value) {
        @SuppressWarnings("unchecked")
        PVReader<T> pvReader = (PVReader<T>) mock(PVReader.class);
        when(pvReader.getValue()).thenReturn(value);
        
        @SuppressWarnings("unchecked")
        PVReaderEvent<T> event = (PVReaderEvent<T>) mock(PVReaderEvent.class);
        when(event.isValueChanged()).thenReturn(true);
        when(event.getPvReader()).thenReturn(pvReader);
        
        return event;
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
    
    @Test
    public void matchValues1() throws Exception {
        VDouble value1 = newVDouble(3.14);
        VString value2 = newVString("Test", alarmNone(), timeNow());
        PVReaderTestListener<Object> listener = PVReaderTestListener.matchValues(newVDouble(3.14, newTime(value1.getTimestamp())),
                newVString("Test", alarmNone(), newTime(value2.getTimestamp())));
        listener.pvChanged(createValueEvent(value1));
        listener.pvChanged(createValueEvent(value2));
        
        listener.close();
        assertThat(listener.getErrorMessage(), nullValue());
        assertThat(listener.isSuccess(), equalTo(true));
    }
}

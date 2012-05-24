/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import gov.aps.jca.CAException;
import gov.aps.jca.CAStatus;
import gov.aps.jca.Channel;
import gov.aps.jca.Channel.ConnectionState;
import gov.aps.jca.Context;
import gov.aps.jca.Monitor;
import gov.aps.jca.dbr.*;
import gov.aps.jca.event.*;
import java.util.Date;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;
import org.epics.pvmanager.data.VDouble;
import org.epics.util.time.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class JCAVTypeAdapterSetTest {
    
    public JCAVTypeAdapterSetTest() {
    }
    
    public static Channel mockChannel(final DBRType dbrType, final int count, final ConnectionState connState) {
        return new Channel() {

            @Override
            public Context getContext() throws IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void destroy() throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ConnectionListener[] getConnectionListeners() throws IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addConnectionListener(ConnectionListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void removeConnectionListener(ConnectionListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public AccessRightsListener[] getAccessRightsListeners() throws IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addAccessRightsListener(AccessRightsListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void removeAccessRightsListener(AccessRightsListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getName() throws IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public DBRType getFieldType() throws IllegalStateException {
                return dbrType;
            }

            @Override
            public int getElementCount() throws IllegalStateException {
                return count;
            }

            @Override
            public ConnectionState getConnectionState() throws IllegalStateException {
                return connState;
            }

            @Override
            public String getHostName() throws IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getReadAccess() throws IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getWriteAccess() throws IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(byte[] value) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(byte[] value, PutListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(short[] value) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(short[] value, PutListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(int[] value) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(int[] value, PutListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(float[] value) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(float[] value, PutListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(double[] value) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(double[] value, PutListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(String[] value) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void put(String[] value, PutListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putACKT(boolean value) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putACKT(boolean value, PutListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putACKS(Severity severity) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putACKS(Severity severity, PutListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public DBR get(DBRType type, int count) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void get(DBRType type, int count, GetListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Monitor addMonitor(DBRType type, int count, int mask, MonitorListener l) throws CAException, IllegalStateException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Test
    public void DBRFloatToVDouble1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVDouble;
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRFloatToVDouble2() {
        ValueCache<VDouble> cache = new ValueCache<VDouble>(VDouble.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVDouble;
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRFloatToVDouble3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVDouble;
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRFloatToVDouble4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVDouble;
        
        Channel channel = mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Float value = createDBRTimeFloat(3.25F, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VDouble.class));
        VDouble converted = (VDouble) cache.getValue();
        assertThat(converted.getValue(), equalTo(3.25));
        assertThat(converted.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(converted.getAlarmStatus(), equalTo(AlarmStatus.RECORD));
        assertThat(converted.getTimestamp(), equalTo(timestamp));
        assertThat(converted.getUpperDisplayLimit(), equalTo(10.0));
        assertThat(converted.getUpperCtrlLimit(), equalTo(8.0));
        assertThat(converted.getUpperAlarmLimit(), equalTo(6.0));
        assertThat(converted.getUpperWarningLimit(), equalTo(4.0));
        assertThat(converted.getLowerWarningLimit(), equalTo(-4.0));
        assertThat(converted.getLowerAlarmLimit(), equalTo(-6.0));
        assertThat(converted.getLowerCtrlLimit(), equalTo(-8.0));
        assertThat(converted.getLowerDisplayLimit(), equalTo(-10.0));
    }

    @Test
    public void DBRFloatToVDouble5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVDouble;
        
        Channel channel = mockChannel(DBR_Float.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Float value = createDBRTimeFloat(3.25F, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VDouble.class));
        VDouble converted = (VDouble) cache.getValue();
        assertThat(converted.getValue(), equalTo(3.25));
        assertThat(converted.getAlarmSeverity(), equalTo(AlarmSeverity.UNDEFINED));
        assertThat(converted.getAlarmStatus(), equalTo(AlarmStatus.CLIENT));
        assertThat(converted.getTimestamp(), equalTo(timestamp));
        assertThat(converted.getUpperDisplayLimit(), equalTo(10.0));
        assertThat(converted.getUpperCtrlLimit(), equalTo(8.0));
        assertThat(converted.getUpperAlarmLimit(), equalTo(6.0));
        assertThat(converted.getUpperWarningLimit(), equalTo(4.0));
        assertThat(converted.getLowerWarningLimit(), equalTo(-4.0));
        assertThat(converted.getLowerAlarmLimit(), equalTo(-6.0));
        assertThat(converted.getLowerCtrlLimit(), equalTo(-8.0));
        assertThat(converted.getLowerDisplayLimit(), equalTo(-10.0));
    }

    private DBR_CTRL_Double createMetadata() {
        DBR_CTRL_Double meta = new DBR_CTRL_Double();
        meta.setUpperDispLimit(10);
        meta.setUpperCtrlLimit(8);
        meta.setUpperAlarmLimit(6);
        meta.setUpperWarningLimit(4);
        meta.setLowerWarningLimit(-4);
        meta.setLowerAlarmLimit(-6);
        meta.setLowerCtrlLimit(-8);
        meta.setLowerDispLimit(-10);
        return meta;
    }

    private DBR_TIME_Float createDBRTimeFloat(float number, gov.aps.jca.dbr.Severity severity, gov.aps.jca.dbr.Status status, org.epics.util.time.Timestamp timestamp) {
        DBR_TIME_Float value = new DBR_TIME_Float(new float[]{number});
        value.setSeverity(severity);
        value.setStatus(status);
        value.setTimeStamp(new TimeStamp(timestamp.getSec() - DataUtils.TS_EPOCH_SEC_PAST_1970, timestamp.getNanoSec()));
        return value;
    }
}

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
import java.util.AbstractList;
import java.util.Date;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.*;
import org.epics.util.array.CollectionNumbers;
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
        DBR_TIME_Float value = createDBRTimeFloat(new float[]{3.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
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
        DBR_TIME_Float value = createDBRTimeFloat(new float[]{3.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
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

    @Test
    public void DBRDoubleToVDouble1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDouble;
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRDoubleToVDouble2() {
        ValueCache<VDouble> cache = new ValueCache<VDouble>(VDouble.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDouble;
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRDoubleToVDouble3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDouble;
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRDoubleToVDouble4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDouble;
        
        Channel channel = mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Double value = createDBRTimeDouble(new double[]{3.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
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
    public void DBRDoubleToVDouble5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDouble;
        
        Channel channel = mockChannel(DBR_Double.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Double value = createDBRTimeDouble(new double[]{3.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
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

    @Test
    public void DBRByteToVInt1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRByteToVInt2() {
        ValueCache<VInt> cache = new ValueCache<VInt>(VInt.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRByteToVInt3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRByteToVInt4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVInt;
        
        Channel channel = mockChannel(DBR_Byte.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Byte value = createDBRTimeByte(new byte[]{32}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VInt.class));
        VInt converted = (VInt) cache.getValue();
        assertThat(converted.getValue(), equalTo(32));
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
    public void DBRByteToVInt5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVInt;
        
        Channel channel = mockChannel(DBR_Byte.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Byte value = createDBRTimeByte(new byte[]{32}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VInt.class));
        VInt converted = (VInt) cache.getValue();
        assertThat(converted.getValue(), equalTo(32));
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

    @Test
    public void DBRShortToVInt1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRShortToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Short.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Short.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRShortToVInt2() {
        ValueCache<VInt> cache = new ValueCache<VInt>(VInt.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRShortToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Short.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Short.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRShortToVInt3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRShortToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Short.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Short.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRShortToVInt4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRShortToVInt;
        
        Channel channel = mockChannel(DBR_Short.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Short value = createDBRTimeShort(new short[]{32}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VInt.class));
        VInt converted = (VInt) cache.getValue();
        assertThat(converted.getValue(), equalTo(32));
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
    public void DBRShortToVInt5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRShortToVInt;
        
        Channel channel = mockChannel(DBR_Short.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Short value = createDBRTimeShort(new short[]{32}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VInt.class));
        VInt converted = (VInt) cache.getValue();
        assertThat(converted.getValue(), equalTo(32));
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

    @Test
    public void DBRIntToVInt1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRIntToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Int.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Int.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRIntToVInt2() {
        ValueCache<VInt> cache = new ValueCache<VInt>(VInt.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRIntToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Int.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Int.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRIntToVInt3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRIntToVInt;
        assertThat(adapter.match(cache, mockChannel(DBR_Int.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Int.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRIntToVInt4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRIntToVInt;
        
        Channel channel = mockChannel(DBR_Int.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Int value = createDBRTimeInt(new int[]{32}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VInt.class));
        VInt converted = (VInt) cache.getValue();
        assertThat(converted.getValue(), equalTo(32));
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
    public void DBRIntToVInt5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRIntToVInt;
        
        Channel channel = mockChannel(DBR_Int.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Int value = createDBRTimeInt(new int[]{32}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VInt.class));
        VInt converted = (VInt) cache.getValue();
        assertThat(converted.getValue(), equalTo(32));
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

    @Test
    public void DBRStringToVString1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRStringToVString;
        assertThat(adapter.match(cache, mockChannel(DBR_String.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_String.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRStringToVString2() {
        ValueCache<VString> cache = new ValueCache<VString>(VString.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRStringToVString;
        assertThat(adapter.match(cache, mockChannel(DBR_String.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_String.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRStringToVString3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRStringToVString;
        assertThat(adapter.match(cache, mockChannel(DBR_String.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_String.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRStringToVString4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRStringToVString;
        
        Channel channel = mockChannel(DBR_String.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_String value = createDBRTimeString(new String[]{"32"}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(null, event));
        
        assertThat(cache.getValue(), instanceOf(VString.class));
        VString converted = (VString) cache.getValue();
        assertThat(converted.getValue(), equalTo("32"));
        assertThat(converted.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(converted.getAlarmStatus(), equalTo(AlarmStatus.RECORD));
        assertThat(converted.getTimestamp(), equalTo(timestamp));
    }

    @Test
    public void DBRStringToVString5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRStringToVString;
        
        Channel channel = mockChannel(DBR_String.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_String value = createDBRTimeString(new String[]{"32"}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(null, event));
        
        assertThat(cache.getValue(), instanceOf(VString.class));
        VString converted = (VString) cache.getValue();
        assertThat(converted.getValue(), equalTo("32"));
        assertThat(converted.getAlarmSeverity(), equalTo(AlarmSeverity.UNDEFINED));
        assertThat(converted.getAlarmStatus(), equalTo(AlarmStatus.CLIENT));
        assertThat(converted.getTimestamp(), equalTo(timestamp));
    }

    @Test
    public void DBREnumToVEnum1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBREnumToVEnum;
        assertThat(adapter.match(cache, mockChannel(DBR_Enum.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Enum.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBREnumToVEnum2() {
        ValueCache<VEnum> cache = new ValueCache<VEnum>(VEnum.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBREnumToVEnum;
        assertThat(adapter.match(cache, mockChannel(DBR_Enum.TYPE, 1, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Enum.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBREnumToVEnum3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBREnumToVEnum;
        assertThat(adapter.match(cache, mockChannel(DBR_Enum.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Enum.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBREnumToVEnum4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBREnumToVEnum;
        
        Channel channel = mockChannel(DBR_Enum.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Enum value = createDBRTimeEnum(new short[]{2}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_LABELS_Enum meta = createMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VEnum.class));
        VEnum converted = (VEnum) cache.getValue();
        assertThat(converted.getValue(), equalTo("Two"));
        assertThat(converted.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(converted.getAlarmStatus(), equalTo(AlarmStatus.RECORD));
        assertThat(converted.getTimestamp(), equalTo(timestamp));
    }

    @Test
    public void DBREnumToVEnum5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBREnumToVEnum;
        
        Channel channel = mockChannel(DBR_Enum.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Enum value = createDBRTimeEnum(new short[]{2}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_LABELS_Enum meta = createMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VEnum.class));
        VEnum converted = (VEnum) cache.getValue();
        assertThat(converted.getValue(), equalTo("Two"));
        assertThat(converted.getAlarmSeverity(), equalTo(AlarmSeverity.UNDEFINED));
        assertThat(converted.getAlarmStatus(), equalTo(AlarmStatus.CLIENT));
        assertThat(converted.getTimestamp(), equalTo(timestamp));
    }

    @Test
    public void DBRFloatToVFloatArray1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVFloatArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 5, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRFloatToVFloatArray2() {
        ValueCache<VFloatArray> cache = new ValueCache<VFloatArray>(VFloatArray.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVFloatArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 5, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRFloatToVFloatArray3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVFloatArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRFloatToVFloatArray4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVFloatArray;
        
        Channel channel = mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Float value = createDBRTimeFloat(new float[]{3.25F, 3.75F, 4.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VFloatArray.class));
        VFloatArray converted = (VFloatArray) cache.getValue();
        assertThat(CollectionNumbers.toDoubleArray(converted.getData()), equalTo(new double[]{3.25, 3.75, 4.25}));
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
    public void DBRFloatToVFloatArray5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRFloatToVFloatArray;
        
        Channel channel = mockChannel(DBR_Float.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Float value = createDBRTimeFloat(new float[]{3.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VFloatArray.class));
        VFloatArray converted = (VFloatArray) cache.getValue();
        assertThat(CollectionNumbers.toDoubleArray(converted.getData()), equalTo(new double[]{3.25}));
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

    @Test
    public void DBRDoubleToVDoubleArray1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDoubleArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 5, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRDoubleToVDoubleArray2() {
        ValueCache<VDoubleArray> cache = new ValueCache<VDoubleArray>(VDoubleArray.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDoubleArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 5, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRDoubleToVDoubleArray3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDoubleArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Float.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRDoubleToVDoubleArray4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDoubleArray;
        
        Channel channel = mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Double value = createDBRTimeDouble(new double[]{3.25, 3.75, 4.25}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VDoubleArray.class));
        VDoubleArray converted = (VDoubleArray) cache.getValue();
        assertThat(CollectionNumbers.toDoubleArray(converted.getData()), equalTo(new double[]{3.25, 3.75, 4.25}));
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
    public void DBRDoubleToVDoubleArray5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDoubleArray;
        
        Channel channel = mockChannel(DBR_Double.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Double value = createDBRTimeDouble(new double[]{3.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VDoubleArray.class));
        VDoubleArray converted = (VDoubleArray) cache.getValue();
        assertThat(CollectionNumbers.toDoubleArray(converted.getData()), equalTo(new double[]{3.25}));
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

    @Test
    public void DBRByteToVByteArray1() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVByteArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 5, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRByteToVByteArray2() {
        ValueCache<VByteArray> cache = new ValueCache<VByteArray>(VByteArray.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVByteArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 5, ConnectionState.CONNECTED)), equalTo(1));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRByteToVByteArray3() {
        ValueCache<String> cache = new ValueCache<String>(String.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVByteArray;
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Byte.TYPE, 5, ConnectionState.CONNECTED)), equalTo(0));
        assertThat(adapter.match(cache, mockChannel(DBR_Double.TYPE, 1, ConnectionState.CONNECTED)), equalTo(0));
    }

    @Test
    public void DBRByteToVByteArray4() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVByteArray;
        
        Channel channel = mockChannel(DBR_Byte.TYPE, 1, ConnectionState.CONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Byte value = createDBRTimeByte(new byte[]{3, 4, 5}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VByteArray.class));
        VByteArray converted = (VByteArray) cache.getValue();
        assertThat(CollectionNumbers.toDoubleArray(converted.getData()), equalTo(new double[]{3, 4, 5}));
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
    public void DBRByteToVByteArray5() {
        ValueCache<Object> cache = new ValueCache<Object>(Object.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRByteToVByteArray;
        
        Channel channel = mockChannel(DBR_Byte.TYPE, 1, ConnectionState.DISCONNECTED);
        Timestamp timestamp = Timestamp.of(1234567,1234);
        DBR_TIME_Byte value = createDBRTimeByte(new byte[]{3}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        MonitorEvent event = new MonitorEvent(channel, value, CAStatus.NORMAL);
        
        adapter.updateCache(cache, channel, new JCAMessagePayload(meta, event));
        
        assertThat(cache.getValue(), instanceOf(VByteArray.class));
        VByteArray converted = (VByteArray) cache.getValue();
        assertThat(CollectionNumbers.toDoubleArray(converted.getData()), equalTo(new double[]{3}));
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

    private DBR_CTRL_Double createNumericMetadata() {
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

    private DBR_LABELS_Enum createMetadata() {
        DBR_LABELS_Enum meta = new DBR_LABELS_Enum();
        meta.setLabels(new String[] {"Zero", "One", "Two", "Three"});
        return meta;
    }

    private DBR_TIME_Float createDBRTimeFloat(float[] data, gov.aps.jca.dbr.Severity severity, gov.aps.jca.dbr.Status status, org.epics.util.time.Timestamp timestamp) {
        DBR_TIME_Float value = new DBR_TIME_Float(data);
        value.setSeverity(severity);
        value.setStatus(status);
        value.setTimeStamp(new TimeStamp(timestamp.getSec() - DataUtils.TS_EPOCH_SEC_PAST_1970, timestamp.getNanoSec()));
        return value;
    }

    private DBR_TIME_Double createDBRTimeDouble(double[] data, gov.aps.jca.dbr.Severity severity, gov.aps.jca.dbr.Status status, org.epics.util.time.Timestamp timestamp) {
        DBR_TIME_Double value = new DBR_TIME_Double(data);
        value.setSeverity(severity);
        value.setStatus(status);
        value.setTimeStamp(new TimeStamp(timestamp.getSec() - DataUtils.TS_EPOCH_SEC_PAST_1970, timestamp.getNanoSec()));
        return value;
    }

    private DBR_TIME_Byte createDBRTimeByte(byte[] data, gov.aps.jca.dbr.Severity severity, gov.aps.jca.dbr.Status status, org.epics.util.time.Timestamp timestamp) {
        DBR_TIME_Byte value = new DBR_TIME_Byte(data);
        value.setSeverity(severity);
        value.setStatus(status);
        value.setTimeStamp(new TimeStamp(timestamp.getSec() - DataUtils.TS_EPOCH_SEC_PAST_1970, timestamp.getNanoSec()));
        return value;
    }

    private DBR_TIME_Short createDBRTimeShort(short[] data, gov.aps.jca.dbr.Severity severity, gov.aps.jca.dbr.Status status, org.epics.util.time.Timestamp timestamp) {
        DBR_TIME_Short value = new DBR_TIME_Short(data);
        value.setSeverity(severity);
        value.setStatus(status);
        value.setTimeStamp(new TimeStamp(timestamp.getSec() - DataUtils.TS_EPOCH_SEC_PAST_1970, timestamp.getNanoSec()));
        return value;
    }

    private DBR_TIME_Int createDBRTimeInt(int[] data, gov.aps.jca.dbr.Severity severity, gov.aps.jca.dbr.Status status, org.epics.util.time.Timestamp timestamp) {
        DBR_TIME_Int value = new DBR_TIME_Int(data);
        value.setSeverity(severity);
        value.setStatus(status);
        value.setTimeStamp(new TimeStamp(timestamp.getSec() - DataUtils.TS_EPOCH_SEC_PAST_1970, timestamp.getNanoSec()));
        return value;
    }

    private DBR_TIME_String createDBRTimeString(String[] data, gov.aps.jca.dbr.Severity severity, gov.aps.jca.dbr.Status status, org.epics.util.time.Timestamp timestamp) {
        DBR_TIME_String value = new DBR_TIME_String(data);
        value.setSeverity(severity);
        value.setStatus(status);
        value.setTimeStamp(new TimeStamp(timestamp.getSec() - DataUtils.TS_EPOCH_SEC_PAST_1970, timestamp.getNanoSec()));
        return value;
    }

    private DBR_TIME_Enum createDBRTimeEnum(short[] data, gov.aps.jca.dbr.Severity severity, gov.aps.jca.dbr.Status status, org.epics.util.time.Timestamp timestamp) {
        DBR_TIME_Enum value = new DBR_TIME_Enum(data);
        value.setSeverity(severity);
        value.setStatus(status);
        value.setTimeStamp(new TimeStamp(timestamp.getSec() - DataUtils.TS_EPOCH_SEC_PAST_1970, timestamp.getNanoSec()));
        return value;
    }
}

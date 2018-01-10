/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.ChannelHandlerReadSubscription;
import org.diirt.datasource.ChannelWriteCallback;
import org.diirt.datasource.MultiplexedChannelHandler;
import org.diirt.datasource.ValueCache;

import org.epics.pvaccess.client.Channel;
import org.epics.pvaccess.client.Channel.ConnectionState;
import org.epics.pvaccess.client.ChannelProvider;
import org.epics.pvaccess.client.ChannelPut;
import org.epics.pvaccess.client.ChannelPutRequester;
import org.epics.pvaccess.client.ChannelRequester;
import org.epics.pvaccess.client.GetFieldRequester;
import org.epics.pvdata.copy.CreateRequest;
import org.epics.pvdata.factory.ConvertFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.misc.BitSet;
import org.epics.pvdata.monitor.Monitor;
import org.epics.pvdata.monitor.MonitorElement;
import org.epics.pvdata.monitor.MonitorRequester;
import org.epics.pvdata.pv.Convert;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.MessageType;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Status;
import org.epics.pvdata.pv.StringArrayData;
import org.epics.pvdata.pv.Structure;
import org.diirt.util.array.CollectionNumbers;
import org.diirt.util.array.ListNumber;

/**
 *
 * @author msekoranja
 */
public class PVAChannelHandler extends
                MultiplexedChannelHandler<PVAChannelHandler, PVStructure> implements
                ChannelRequester, GetFieldRequester, ChannelPutRequester, MonitorRequester {

        private final ChannelProvider pvaChannelProvider;
        private final short priority;
        private final PVATypeSupport pvaTypeSupport;

        private volatile Channel channel = null;

        private final AtomicBoolean monitorCreated = new AtomicBoolean(false);
        private final AtomicLong monitorLossCounter = new AtomicLong(0);
        //private volatile Monitor monitor = null;

        private volatile Field channelType = null;
        private volatile boolean isChannelEnumType = false;

        private final AtomicBoolean channelPutCreated = new AtomicBoolean(false);
        private volatile ChannelPut channelPut = null;
        private volatile PVStructure channelPutStructure = null;
        private volatile BitSet bitSet = null;
        private volatile PVField channelPutValueField = null;


        private static final Logger logger = Logger.getLogger(PVAChannelHandler.class.getName());

        private static CreateRequest createRequest = CreateRequest.create();
        private static PVStructure allPVRequest = createRequest.createRequest("field()");
        private static PVStructure standardPutPVRequest = createRequest.createRequest("field(value)");
        private static PVStructure enumPutPVRequest = createRequest.createRequest("field(value.index)");

        private static final String PVREQUEST_PREFIX = "?request=";
        private final PVStructure pvRequest;
        private final String extractPVField;

        public static PVAChannelHandler create(String channelName,
                        ChannelProvider channelProvider, short priority,
                        PVATypeSupport typeSupport) {

                int pos = channelName.indexOf(PVREQUEST_PREFIX);
                if (pos == -1)
                {
                        return new PVAChannelHandler(channelName, null, channelProvider, priority, typeSupport);
                }
                else
                {
                        String pvRequestString = channelName.substring(pos+PVREQUEST_PREFIX.length());
                        channelName = channelName.substring(0, pos);
                        return new PVAChannelHandler(channelName, pvRequestString, channelProvider, priority, typeSupport);
                }

        }

        public PVAChannelHandler(String channelName, String pvRequestString,
                        ChannelProvider channelProvider, short priority,
                        PVATypeSupport typeSupport) {
                super(channelName);
                this.pvRequest = (pvRequestString != null) ? createRequest.createRequest(pvRequestString) : null;
                this.pvaChannelProvider = channelProvider;
                this.priority = priority;
                this.pvaTypeSupport = typeSupport;

                if (pvRequest != null)
                {
                        PVStructure field = pvRequest.getStructureField("field");
                        extractPVField = getOnlyChildFieldName(field);
                }
                else
                        extractPVField = null;

                // NOTE: mind "return" above
        }

        private static final String _OPTIONS = "_options";
        private static final String TAKE_PARENT = _OPTIONS;
        private static final String getOnlyChildFieldName(PVStructure field)
        {
                if (field != null)
                {
                        String[] fieldNames = field.getStructure().getFieldNames();
                        if (fieldNames.length > 0)
                        {
                                String name = null;
                                for (int i = 0; i < fieldNames.length; i++)
                                {
                                        // ignore options
                                        if (!fieldNames[0].equals(_OPTIONS))
                                        {
                                                if (name == null)
                                                        name = fieldNames[0];
                                                else
                                                        return null;
                                        }
                                }

                                if (name == null)
                                {
                                        // only "_options" field, that's OK
                                        return TAKE_PARENT;
                                }
                                else
                                {
                                        String childName = getOnlyChildFieldName(field.getStructureField(name));
                                        if (childName == null)
                                                return null;
                                        else if (childName.equals(_OPTIONS))
                                                return name;
                                        else
                                                return name + "." + childName;
                                }
                        }
                        else
                        {
                                // no options, no subfield(s)
                                return TAKE_PARENT;
                        }
                }
                else
                        return null;
        }


        /**
         * @return the channel
         */
        public Channel getChannel() {
                return channel;
        }

        /**
         * @return the channelType
         */
        public Field getChannelType() {
                return channelType;
        }

        public String getExtractFieldName() {
                return extractPVField;
        }

        @Override
        public String getRequesterName() {
                return this.getClass().getName();
        }

        @Override
        public void message(String message, MessageType messageType) {
                logger.log(toLoggerLevel(messageType), message);
        }

        /**
         * Converts MessageType to Java Logging API Level.
         * @param messageType pvData message type.
         * @return Corresponded Java Logging API Level.
         */
        public static Level toLoggerLevel(MessageType messageType) {
                switch (messageType) {
                case info:
                        return Level.INFO;
                case warning:
                        return Level.WARNING;
                case error:
                case fatalError:
                        return Level.SEVERE;
                default:
                        return Level.INFO;
                }
        }

        private void reportStatus(String message, Status status)
        {
                if (!status.isSuccess()) {
                        logger.log(Level.WARNING, message + ": " + status.getMessage());

                        // for developers
                        String dump = status.getStackDump();
                        if (dump != null && !dump.isEmpty())
                                logger.log(Level.FINER, message + ": " + status.getMessage() + ", cause:\n" + dump);
                }
        }

        @Override
        public void connect() {
                pvaChannelProvider.createChannel(getChannelName(), this, priority);
        }

        @Override
        public void channelCreated(Status status, Channel channel) {
                reportStatus("Failed to create channel instance '" + channel.getChannelName(), status);
                this.channel = channel;
        }

        @Override
        public void channelStateChange(Channel channel, ConnectionState connectionState) {
                try {

                        // introspect
                        if (connectionState == ConnectionState.CONNECTED) {
                                if (extractPVField == null)
                                        channel.getField(this, null);
                                else
                                        channel.getField(this, extractPVField);
                        }
                        else
                        {
                                processConnection(this);
                        }

                } catch (Exception ex) {
                        reportExceptionToAllReadersAndWriters(ex);
                }
        }

        /* (non-Javadoc)
         * @see org.epics.pvaccess.client.GetFieldRequester#getDone(org.epics.pvdata.pv.Status, org.epics.pvdata.pv.Field)
         */
        @Override
        public void getDone(Status status, Field field) {
                reportStatus("Failed to instrospect channel '" + channel.getChannelName() + "'", status);

                if (status.isSuccess())
                {
                        channelType = field;

                        Field valueField = (channelType instanceof Structure) ? ((Structure)channelType).getField("value") : null;
                        if (valueField != null && valueField.getID().equals("enum_t"))
                        {
                                isChannelEnumType = true;
                                // TODO could create a monitor just to get value.choices
                        }
                        else
                                isChannelEnumType = false;
                }

                processConnection(this);
        }

        @Override
        public boolean isConnected(PVAChannelHandler channel) {
                final Channel c = channel.getChannel();
                return c != null && c.isConnected();
        }

    @Override
    protected boolean isWriteConnected(PVAChannelHandler channel) {
        // NOTE: access-rights not yet supported
                final Channel c = channel.getChannel();
                return c != null && c.isConnected();
    }

    @Override
    public synchronized Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<String, Object>();
        if (channel != null) {
            properties.put("Channel name", channel.getChannelName());
            if (pvRequest != null)
                properties.put("User pvRequest", pvRequest.toString());
            properties.put("Connection state", channel.getConnectionState().name());
            properties.put("Provider name", channel.getProvider().getProviderName());
            if (channel.getConnectionState() == Channel.ConnectionState.CONNECTED) {
                properties.put("Remote address", channel.getRemoteAddress());
                properties.put("Channel type", channelType.getID());
                //properties.put("Read access", channel.getReadAccess());
                //properties.put("Write access", channel.getWriteAccess());
            }
            properties.put("Monitor loss count", monitorLossCounter.get());
        }
        return properties;
    }

    @Override
        public void disconnect() {
                // Close the channel
                try {
                        channel.destroy();
                } finally {
                        channel = null;

                        //monitor = null;
                        monitorCreated.set(false);

                        channelType = null;

                        channelPut = null;
                        channelPutValueField = null;
                        channelPutCreated.set(false);
                }
        }

        static class WriteRequest
        {
                private final Object newValue;
                private final ChannelWriteCallback callback;

                public WriteRequest(Object newValue, ChannelWriteCallback callback) {
                        this.newValue = newValue;
                        this.callback = callback;
                }

                public Object getNewValue() {
                        return newValue;
                }

                public ChannelWriteCallback getCallback() {
                        return callback;
                }
        }

        private final LinkedList<WriteRequest> writeRequests = new LinkedList<WriteRequest>();

        @Override
        public void write(Object newValue, ChannelWriteCallback callback) {

                boolean wasEmpty;
                synchronized (writeRequests)
                {
                        wasEmpty = writeRequests.isEmpty();
                        writeRequests.add(new WriteRequest(newValue, callback));
                }

                if (!channelPutCreated.getAndSet(true))
                {
                        channel.createChannelPut(this, isChannelEnumType ? enumPutPVRequest : standardPutPVRequest);
                }
                else if (wasEmpty)
                {
                        doNextWrite();
                }
        }

        private void doNextWrite()
        {
                WriteRequest writeRequest;
                synchronized (writeRequests)
                {
                        writeRequest = writeRequests.peek();
                }

                if (writeRequest != null)
                {
                        try {
                                if (channelPutValueField == null)
                                        throw new RuntimeException("No 'value' field");

                                fromObject(channelPutValueField, writeRequest.getNewValue());
                                channelPut.put(channelPutStructure, bitSet);
                        } catch (Exception ex) {
                                writeRequests.poll();
                                writeRequest.getCallback().channelWritten(ex);
                        }
                }

        }

        @Override
        public void channelPutConnect(Status status, ChannelPut channelPut, Structure putStructure) {
                reportStatus("Failed to create ChannelPut instance", status);

                if (status.isSuccess())
                {
                        this.channelPut = channelPut;

                        if (channelPutStructure == null ||
                                !channelPutStructure.getStructure().equals(putStructure))
                        {
                                channelPutStructure = PVDataFactory.getPVDataCreate().createPVStructure(putStructure);
                                bitSet = new BitSet(channelPutStructure.getNumberFields());
                        }

                        if (isChannelEnumType)
                        {
                                // handle inconsistent behavior
                                this.channelPutValueField = channelPutStructure.getSubField("value");
                                if (this.channelPutValueField instanceof PVStructure)
                                        this.channelPutValueField = ((PVStructure)channelPutValueField).getSubField("index");
                        }
                        else
                        {
                                this.channelPutValueField = channelPutStructure.getSubField("value");
                        }


                        // set BitSet
                        bitSet.clear(); // re-connect case
                        if (this.channelPutValueField != null)
                                bitSet.set(channelPutValueField.getFieldOffset());
                }

                doNextWrite();
        }

        @Override
        public void putDone(Status status, ChannelPut channePut) {
                reportStatus("Failed to put value", status);

                WriteRequest writeRequest;
                synchronized (writeRequests)
                {
                        writeRequest = writeRequests.poll();
                }

                if (writeRequest != null)
                {
                        if (status.isSuccess())
                        {
                                writeRequest.getCallback().channelWritten(null);
                        }
                        else
                        {
                                writeRequest.getCallback().channelWritten(new Exception(status.getMessage()));
                        }

                        doNextWrite();
                }

        }

        @Override
        public void getDone(Status status, ChannelPut channelPut, PVStructure pvStructure, BitSet bitSet) {
                // never used, i.e. ChannelPut.get() never called
        }

        private final static Convert convert = ConvertFactory.getConvert();

        // TODO check if non-V types can ever be given as newValue
        private final void fromObject(PVField field, Object newValue)
        {
                // enum support
                if (isChannelEnumType)
                {
                        // value.index int field expected
                        PVInt indexPutField = (PVInt)channelPutValueField;

                        int index = -1;
                        if (newValue instanceof Number)
                        {
                                index = ((Number)newValue).intValue();
                        }
                        else if (newValue instanceof String)
                        {
                                String nv = (String)newValue;

                                PVStructure lastValue = getLastMessagePayload();
                                if (lastValue == null)
                                        throw new IllegalArgumentException("no monitor on '" + getChannelName() +"' created to get list of valid enum choices");

                                PVStringArray pvChoices = (PVStringArray)lastValue.getSubField("value.choices");
                                StringArrayData data = new StringArrayData();
                                pvChoices.get(0, pvChoices.getLength(), data);
                                final String[] choices = data.data;

                                for (int i = 0; i < choices.length; i++)
                                {
                                        if (nv.equals(choices[i]))
                                        {
                                                index = i;
                                                break;
                                        }
                                }

                                // fallback: try to convert string to an number (index)
                                if (index == -1)
                                {
                                        try {
                                                int ix = Integer.parseInt(nv);
                                                if (ix >= 0 && ix < choices.length)
                                                        index = ix;
                                        } catch (Throwable th) {
                                                // failed to convert, noop
                                        }
                                }

                                if (index == -1)
                                        throw new IllegalArgumentException("enumeration '" + nv +"' is not a valid choice");
                        }

                        indexPutField.put(index);

                        return;
                }

        if (channelPutValueField instanceof PVScalar)
        {
                if (newValue instanceof Double)
                                convert.fromDouble((PVScalar)field, ((Double)newValue).doubleValue());
                        else if (newValue instanceof Integer)
                                convert.fromInt((PVScalar)field, ((Integer)newValue).intValue());
                        else if (newValue instanceof String)
                                convert.fromString((PVScalar)field, (String)newValue);
                        else if (newValue instanceof Byte)
                                convert.fromByte((PVScalar)field, ((Byte)newValue).byteValue());
                        else if (newValue instanceof Short)
                                convert.fromShort((PVScalar)field, ((Short)newValue).shortValue());
                        else if (newValue instanceof Long)
                                convert.fromLong((PVScalar)field, ((Long)newValue).longValue());
                        else if (newValue instanceof Float)
                                convert.fromFloat((PVScalar)field, ((Float)newValue).floatValue());
                        else if (newValue instanceof Boolean)
                                //  TODO no convert.fromBoolean
                                //convert.fromBoolean((PVScalar)field, ((Boolean)newValue).booleanValue());
                                convert.fromByte((PVScalar)field, ((Boolean)newValue).booleanValue() ? (byte)1 : (byte)0);
                else
                        throw new RuntimeException("Unsupported write, cannot put '" + newValue.getClass() + "' into scalar '" + channelPutValueField.getField() + "'");
        }
        else if (channelPutValueField instanceof PVScalarArray)
        {
            // if it's a ListNumber, extract the array
            if (newValue instanceof ListNumber) {
                ListNumber data = (ListNumber) newValue;
                Object wrappedArray = CollectionNumbers.wrappedArray(data);
                if (wrappedArray == null) {
                    newValue = CollectionNumbers.doubleArrayCopyOf(data);
                } else {
                    newValue = wrappedArray;
                }
            }
            else if (!newValue.getClass().isArray())
            {
                // create an array
                Object newValueArray = Array.newInstance(newValue.getClass(), 1);
                Array.set(newValueArray, 0, newValue);
                newValue = newValueArray;
            }

            if (newValue instanceof double[])
                        convert.fromDoubleArray((PVScalarArray)field, 0, ((double[])newValue).length, (double[])newValue, 0);
                else if (newValue instanceof int[])
                        convert.fromIntArray((PVScalarArray)field, 0, ((int[])newValue).length, (int[])newValue, 0);
                else if (newValue instanceof String[])
                        convert.fromStringArray((PVScalarArray)field, 0, ((String[])newValue).length, (String[])newValue, 0);
            // special case from string to array
                else if (newValue instanceof String)
                {
                        String str = ((String)newValue).trim();

                        // remove []
                        if (str.charAt(0) == '[' && str.charAt(str.length()-1) == ']')
                                str = str.substring(1, str.length()-1);

                        // split on commas and whitespaces
                        String[] splitValues = str.split("[,\\s]+");
                        convert.fromStringArray((PVScalarArray)field, 0, splitValues.length, splitValues, 0);
                }

                else if (newValue instanceof byte[])
                        convert.fromByteArray((PVScalarArray)field, 0, ((byte[])newValue).length, (byte[])newValue, 0);
                else if (newValue instanceof short[])
                        convert.fromShortArray((PVScalarArray)field, 0, ((short[])newValue).length, (short[])newValue, 0);
                else if (newValue instanceof long[])
                        convert.fromLongArray((PVScalarArray)field, 0, ((long[])newValue).length, (long[])newValue, 0);
                else if (newValue instanceof float[])
                        convert.fromFloatArray((PVScalarArray)field, 0, ((float[])newValue).length, (float[])newValue, 0);
                else if (newValue instanceof boolean[])
                {
                        boolean[] bArray = (boolean[])newValue;
                        byte[] byteArray = new byte[bArray.length];
                        for (int i = 0; i < bArray.length; i++)
                                byteArray[i] = bArray[i] ? (byte)1 : (byte)0;
                        convert.fromByteArray((PVScalarArray)field, 0, byteArray.length, byteArray, 0);
                }
                else
                        throw new RuntimeException("Unsupported write, cannot put '" + newValue.getClass() + "' into array'" + channelPutValueField.getField() + "'");
        }
                else
                        throw new RuntimeException("Unsupported write, cannot put '" + newValue.getClass() + "' into '" + channelPutValueField.getField() + "'");


        }




        @Override
        protected PVATypeAdapter findTypeAdapter(
                        ValueCache<?> cache, PVAChannelHandler connection) {
                PVATypeAdapter pta = null;
                try     {
                        pta = pvaTypeSupport.find(cache, connection);
                } catch (Throwable th) { th.printStackTrace(); }
                return pta;
        }

        @Override
        public void addReader(ChannelHandlerReadSubscription subscription) {
                super.addReader(subscription);

                if (!monitorCreated.getAndSet(true))
                {
                        // TODO remove this....
                        for (int i = 0; i < 100 && channel.getConnectionState() == ConnectionState.NEVER_CONNECTED; i++)
                        {
                                try {
                                        Thread.sleep(100);
                                } catch (InterruptedException e) { }
                        }
                        // TODO optimize fields
                        channel.createMonitor(this, pvRequest != null ? pvRequest : allPVRequest);
                }
        }

        /* (non-Javadoc)
         * @see org.epics.pvdata.monitor.MonitorRequester#monitorConnect(org.epics.pvdata.pv.Status, org.epics.pvdata.monitor.Monitor, org.epics.pvdata.pv.Structure)
         */
        @Override
        public void monitorConnect(Status status, Monitor monitor, Structure structure) {
                reportStatus("Failed to create monitor", status);

                if (status.isSuccess())
                {
                        //this.monitor = monitor;
                        monitor.start();
                }
        }

        /* (non-Javadoc)
         * @see org.epics.pvdata.monitor.MonitorRequester#monitorEvent(org.epics.pvdata.monitor.Monitor)
         */
        @Override
        public void monitorEvent(Monitor monitor) {
                MonitorElement monitorElement;
                while ((monitorElement = monitor.poll()) != null)
                {
                        if (monitorElement.getOverrunBitSet().cardinality() > 0)
                                monitorLossCounter.incrementAndGet();

                        // TODO combine bitSet, etc.... do we need to copy structure?
                        processMessage(monitorElement.getPVStructure());
                        monitor.release(monitorElement);
                }
        }

        /* (non-Javadoc)
         * @see org.epics.pvdata.monitor.MonitorRequester#unlisten(org.epics.pvdata.monitor.Monitor)
         */
        @Override
        public void unlisten(Monitor monitor) {
                // TODO Auto-generated method stub
        }

        @Override
        public String toString() {
                return "PVAChannelHandler [getChannelName()=" + getChannelName() + "]";
        }
}

/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.loc;

import org.diirt.datasource.ChannelWriteCallback;
import org.diirt.datasource.ChannelHandlerReadSubscription;
import org.diirt.datasource.MultiplexedChannelHandler;
import org.diirt.datasource.ChannelHandlerWriteSubscription;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.diirt.vtype.ValueFactory.*;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;

/**
 * Implementation for channels of a {@link LocalDataSource}.
 *
 * @author carcassi
 */
class LocalChannelHandler extends MultiplexedChannelHandler<Object, Object> {

    private static Logger log = Logger.getLogger(LocalChannelHandler.class.getName());

    LocalChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    public void connect() {
        processConnection(new Object());
    }

    @Override
    public void disconnect() {
        initialArguments = null;
        type = null;
        processConnection(null);
    }

    @Override
    protected synchronized void addReader(ChannelHandlerReadSubscription subscription) {
        // Override for test visibility purposes
        super.addReader(subscription);
    }

    @Override
    protected synchronized void addWriter(ChannelHandlerWriteSubscription subscription) {
        // Override for test visibility purposes
        super.addWriter(subscription);
    }

    @Override
    protected synchronized void removeReader(ChannelHandlerReadSubscription subscription) {
        // Override for test visibility purposes
        super.removeReader(subscription);
    }

    @Override
    protected synchronized void removeWrite(ChannelHandlerWriteSubscription subscription) {
        // Override for test visibility purposes
        super.removeWrite(subscription);
    }

    private Object checkValue(Object value) {
        if (type != null && !type.isInstance(value)) {
            throw new IllegalArgumentException("Value " + value + " is not of type " + type.getSimpleName());
        }
        return value;
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        try {
            if (VEnum.class.equals(type)) {
                // Handle enum writes
                int newIndex = -1;
                VEnum firstEnum = (VEnum) initialValue;
                List<String> labels = firstEnum.getLabels();
                if (newValue instanceof Number) {
                    newIndex = ((Number) newValue).intValue();
                }else if (newValue instanceof String) {
                    newIndex = labels.indexOf((String) newValue);
                    // Only if the String is not in the labels, try and
                    // parse a number.
                    if (newIndex == -1) {
                        String value = (String) newValue;
                        try {
                            newIndex = Double.valueOf(value).intValue();
                        } catch (NumberFormatException ex) {
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Value" + newValue + " can not be accepted by VEnum.");
                }
                newValue = ValueFactory.newVEnum(newIndex, firstEnum.getLabels(), alarmNone(), timeNow());
            } else if (VString.class.equals(type) && newValue instanceof String) {
                newValue = ValueFactory.newVString((String)newValue, alarmNone(), timeNow());
            } else {
                // If the string can be parsed to a number, do it
                if (newValue instanceof String) {
                    String value = (String) newValue;
                    try {
                        newValue = Double.valueOf(value);
                    } catch (NumberFormatException ex) {
                        // Not a double - continue
                    }
                }
                // If new value is not a VType, try to convert it
                if (!(newValue instanceof VType)) {
                    newValue = checkValue(ValueFactory.toVTypeChecked(newValue));
                }
            }
            processMessage(newValue);
            callback.channelWritten(null);
        } catch (Exception ex) {
            callback.channelWritten(ex);
        }
    }

    @Override
    protected boolean isWriteConnected(Object payload) {
        return isConnected(payload);
    }

    private Object initialArguments;
    private Object initialValue;
    private Class<?> type;

    synchronized void setInitialValue(Object value) {
        if (initialArguments != null && !initialArguments.equals(value)) {
            String message = "Different initialization for local channel " + getChannelName() + ": " + value + " but was " + initialArguments;
            log.log(Level.WARNING, message);
            throw new RuntimeException(message);
        }
        initialArguments = value;
        if (getLastMessagePayload() == null) {
            if (VEnum.class.equals(type)) {
                List<?> args = (List<?>) initialArguments;
                // TODO error message if not Number
                int index = ((Number) args.get(0)).intValue();
                List<String> labels = (List<String>) args.get(1);
                initialValue = ValueFactory.newVEnum(index, labels, alarmNone(), timeNow());
            } else {
                initialValue = checkValue(ValueFactory.toVTypeChecked(value));
            }
            processMessage(initialValue);
        }
    }

    synchronized void setType(String typeName) {
        if (typeName == null) {
            return;
        }
        Class<?> newType = null;
        if ("VDouble".equals(typeName)) {
            newType = VDouble.class;
        }
        if ("VString".equals(typeName)) {
            newType = VString.class;
        }
        if ("VDoubleArray".equals(typeName)) {
            newType = VDoubleArray.class;
        }
        if ("VStringArray".equals(typeName)) {
            newType = VStringArray.class;
        }
        if ("VTable".equals(typeName)) {
            newType = VTable.class;
        }
        if ("VEnum".equals(typeName)) {
            newType = VEnum.class;
        }
        if (newType == null) {
            throw new IllegalArgumentException("Type " + typeName + " for channel " + getChannelName() + " is not supported by local datasource.");
        }
        if (type != null && !type.equals(newType)) {
            throw new IllegalArgumentException("Type mismatch for channel " + getChannelName() + ": " + typeName + " but was " + type.getSimpleName());
        }
        type = newType;
    }

    @Override
    public synchronized Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("Name", getChannelName());
        properties.put("Type", type);
        properties.put("Initial Value", initialArguments);
        return properties;
    }

}

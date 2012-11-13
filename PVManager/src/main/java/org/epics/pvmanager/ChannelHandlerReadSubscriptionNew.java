/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

/**
 * Groups all the parameters required to add a reader to a ChannelHandler.
 *
 * @author carcassi
 */
public class ChannelHandlerReadSubscriptionNew {

    public ChannelHandlerReadSubscriptionNew(WriteFunction<?> valueWriteFunction, WriteFunction<Exception> exceptionWriteFunction, WriteFunction<Boolean> connWriteFunction) {
        this.valueWriteFunction = valueWriteFunction;
        this.exceptionWriteFunction = exceptionWriteFunction;
        this.connWriteFunction = connWriteFunction;
    }
    
    private final WriteFunction<?> valueWriteFunction;
    private final WriteFunction<Exception> exceptionWriteFunction;
    private final WriteFunction<Boolean> connWriteFunction;

    public WriteFunction<?> getValueWriteFunction() {
        return valueWriteFunction;
    }

    public WriteFunction<Exception> getExceptionWriteFunction() {
        return exceptionWriteFunction;
    }

    public WriteFunction<Boolean> getConnWriteFunction() {
        return connWriteFunction;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.valueWriteFunction != null ? this.valueWriteFunction.hashCode() : 0);
        hash = 67 * hash + (this.exceptionWriteFunction != null ? this.exceptionWriteFunction.hashCode() : 0);
        hash = 67 * hash + (this.connWriteFunction != null ? this.connWriteFunction.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChannelHandlerReadSubscriptionNew other = (ChannelHandlerReadSubscriptionNew) obj;
        if (this.valueWriteFunction != other.valueWriteFunction && (this.valueWriteFunction == null || !this.valueWriteFunction.equals(other.valueWriteFunction))) {
            return false;
        }
        if (this.exceptionWriteFunction != other.exceptionWriteFunction && (this.exceptionWriteFunction == null || !this.exceptionWriteFunction.equals(other.exceptionWriteFunction))) {
            return false;
        }
        if (this.connWriteFunction != other.connWriteFunction && (this.connWriteFunction == null || !this.connWriteFunction.equals(other.connWriteFunction))) {
            return false;
        }
        return true;
    }
    
    
}

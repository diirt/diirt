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
public class ChannelHandlerReadSubscription {

    public ChannelHandlerReadSubscription(ValueCache<?> valueCache, WriteFunction<Exception> exceptionWriteFunction, WriteFunction<Boolean> connWriteFunction) {
        this.valueCache = valueCache;
        this.exceptionWriteFunction = exceptionWriteFunction;
        this.connWriteFunction = connWriteFunction;
    }
    
    private final ValueCache<?> valueCache;
    private final WriteFunction<Exception> exceptionWriteFunction;
    private final WriteFunction<Boolean> connWriteFunction;

    public ValueCache<?> getValueCache() {
        return valueCache;
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
        hash = 67 * hash + (this.valueCache != null ? this.valueCache.hashCode() : 0);
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
        final ChannelHandlerReadSubscription other = (ChannelHandlerReadSubscription) obj;
        if (this.valueCache != other.valueCache && (this.valueCache == null || !this.valueCache.equals(other.valueCache))) {
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

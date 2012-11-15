/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

/**
 *
 * @author carcassi
 */
public class ForwardCache<T, R> implements ValueCache<T> {
    
    private final ValueCache<T> valueCache;
    private final Function<R> forwardFunction;
    private final WriteFunction<R> forwardWriter;

    public ForwardCache(ValueCache<T> valueCache, Function<R> forwardFunction, WriteFunction<R> forwardWriter) {
        this.valueCache = valueCache;
        this.forwardFunction = forwardFunction;
        this.forwardWriter = forwardWriter;
    }

    @Override
    public T getValue() {
        return valueCache.getValue();
    }

    @Override
    public void setValue(T newValue) {
        synchronized(forwardFunction) {
            valueCache.setValue(newValue);
            R forwardValue = forwardFunction.getValue();
            forwardWriter.setValue(forwardValue);
        }
    }

    @Override
    public Class<T> getType() {
        return valueCache.getType();
    }
    
}

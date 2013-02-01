/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

/**
 * A collector that forwards exceptions to the handler.
 *
 * @author carcassi
 */
class LastExceptionCollector extends QueueCollector<Exception> {

    private final WriteFunction<Exception> exceptionHandler;
    
    public LastExceptionCollector(int maxSize, WriteFunction<Exception> exceptionHandler) {
        super(maxSize);
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void writeValue(Exception newValue) {
        super.writeValue(newValue);
        exceptionHandler.writeValue(newValue);
    }
    
}

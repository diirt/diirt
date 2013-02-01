/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
class SafeWriteFunction<T> implements WriteFunction<T> {

    private static final Logger log = Logger.getLogger(ExceptionHandler.class.getName());

    private final WriteFunction<T> delegate;

    public SafeWriteFunction(WriteFunction<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void writeValue(T newValue) {
        try {
            delegate.writeValue(newValue);
        } catch (RuntimeException e) {
            log.log(Level.INFO, "Caught exception", e);
        }
    }
}

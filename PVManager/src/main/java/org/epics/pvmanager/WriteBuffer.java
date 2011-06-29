/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Map;
import org.epics.pvmanager.loc.ChannelWriteCallback;

/**
 * Represents all the values, channel names and ordering information needed
 * for writing
 *
 * @author carcassi
 */
public class WriteBuffer {
    private final Map<String, WriteCache> caches;
    private final ExceptionHandler exceptionHandler;
    private final PVValueWriteListener writeListener;

    public WriteBuffer(Map<String, WriteCache> caches, ExceptionHandler exceptionHandler,
            PVValueWriteListener writeListener) {
        this.caches = caches;
        this.exceptionHandler = exceptionHandler;
        this.writeListener = writeListener;
    }
    
    public Map<String, WriteCache> getWriteCaches() {
        return caches;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public PVValueWriteListener getWriteListener() {
        return writeListener;
    }
    
}

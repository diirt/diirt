/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author carcassi
 */
public class QueueCollector<T> implements Collector<T, List<T>> {
    
    private final Object lock = new Object();
    private List<T> readBuffer;
    private List<T> writeBuffer;
    private int maxSize;

    public QueueCollector(int maxSize) {
        synchronized(lock) {
            this.maxSize = maxSize;
            readBuffer = new ArrayList<>();
            writeBuffer = new ArrayList<>();
        }
    }

    @Override
    public void writeValue(T newValue) {
        synchronized(lock) {
            writeBuffer.add(newValue);
            if (writeBuffer.size() > maxSize) {
                writeBuffer.remove(0);
            }
        }
    }

    @Override
    public List<T> readValue() {
        synchronized(lock) {
            List<T> data = writeBuffer;
            writeBuffer = readBuffer;
            writeBuffer.clear();
            readBuffer = data;
        }
        return readBuffer;
    }

    public void setMaxSize(int maxSize) {
        synchronized(lock) {
            this.maxSize = maxSize;
            while (writeBuffer.size() > maxSize) {
                writeBuffer.remove(0);
            }
        }
    }

    public int getMaxSize() {
        synchronized(lock) {
            return maxSize;
        }
    }
    
}

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
public class MockPVWriteListener<T> implements PVWriterListener {
    
    public static <V> MockPVWriteListener<V> addPVWriteListener(PVWriter<V> pvWriter) {
        MockPVWriteListener<V> listener = new MockPVWriteListener<V>(pvWriter);
        pvWriter.addPVWriterListener(listener);
        return listener;
    }
    
    private int counter;
    private PVWriter<T> pvWriter;

    public MockPVWriteListener(PVWriter<T> pvWriter) {
        this.pvWriter = pvWriter;
    }

    @Override
    public synchronized void pvChanged(PVWriterEvent event) {
        counter++;
    }

    public synchronized int getCounter() {
        return counter;
    }
    
}

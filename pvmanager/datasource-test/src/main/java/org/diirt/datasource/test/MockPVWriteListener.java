/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import org.diirt.datasource.PVWriter;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;

/**
 *
 * @author carcassi
 */
public class MockPVWriteListener<T> implements PVWriterListener<T> {

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

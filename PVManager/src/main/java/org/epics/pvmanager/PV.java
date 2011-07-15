/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

/**
 *
 * @author carcassi
 */
public class PV<R, W> extends PVReader<R> implements PVWriter<W> {
    
    private final PVWriterImpl<W> writer;

    public PV(String name, boolean syncWrite) {
        super(name);
        writer = new PVWriterImpl<W>(syncWrite);
    }
    
    PVWriterImpl<W> getWriteImpl() {
        return writer;
    }

    @Override
    public void addPVValueWriteListener(PVValueWriteListener listener) {
        writer.addPVValueWriteListener(listener);
    }

    @Override
    public void removePVValueChangeListener(PVValueWriteListener listener) {
        writer.removePVValueChangeListener(listener);
    }

    @Override
    public void write(W newValue) {
        writer.write(newValue);
    }

    @Override
    public Exception lastWriteException() {
        return writer.lastWriteException();
    }
    
}

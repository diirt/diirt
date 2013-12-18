/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.integration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;

/**
 *
 * @author carcassi
 */
public abstract class PVReaderCondition<T> {
    
    abstract public boolean accept(PVReader<T> reader, PVReaderEvent<T> event);
    
    public boolean waitOn(PVReader<T> reader, int msTimeout) {
        final CountDownLatch latch = new CountDownLatch(1);
        reader.addPVReaderListener(new PVReaderListener<T>() {

            @Override
            public void pvChanged(PVReaderEvent<T> event) {
                if (accept(event.getPvReader(), event)) {
                    latch.countDown();
                    event.getPvReader().removePVReaderListener(this);
                }
            }
        });
        if (accept(reader, null)) {
            latch.countDown();
        }
        try {
            return latch.await(msTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(PVReaderCondition.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}

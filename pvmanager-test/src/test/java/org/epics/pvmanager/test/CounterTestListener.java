/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;

/**
 *
 * @author carcassi
 */
public class CounterTestListener implements PVReaderListener<Integer> {

    private volatile int nextExpected = 0;
    private volatile boolean failed;

    @Override
    public void pvChanged(PVReaderEvent<Integer> event) {
        if (event.getPvReader().getValue() == null) {
            System.out.println("Fail: expected " + nextExpected + " was null");
            failed = true;
        } else if (event.getPvReader().getValue() != nextExpected) {
            System.out.println("Fail: expected " + nextExpected + " was " + event.getPvReader().getValue());
            failed = true;
        }
        nextExpected++;
    }

    public boolean isFailed() {
        return failed;
    }

    public int getNextExpected() {
        return nextExpected;
    }
    
    
}

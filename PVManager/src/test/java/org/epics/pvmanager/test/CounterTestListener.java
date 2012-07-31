/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.data.VInt;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class CounterTestListener implements PVReaderListener {

    public CounterTestListener(PVReader<VInt> pv) {
        this.pv = pv;
    }
    
    private final PVReader<VInt> pv;
    private volatile int nextExpected = 0;
    private volatile boolean failed;

    @Override
    public void pvChanged() {
        if (pv.getValue() == null) {
            System.out.println("Fail: expected " + nextExpected + " was null");
            failed = true;
        } else if (pv.getValue().getValue() != nextExpected) {
            System.out.println("Fail: expected " + nextExpected + " was " + pv.getValue().getValue());
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

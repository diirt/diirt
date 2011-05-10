/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jca;

import java.util.ArrayList;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVValueChangeListener;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class JCASteadyLongTerm {
    public static void main(String[] args) throws Exception {
        JCADataSource jca = new JCADataSource(JCALibrary.CHANNEL_ACCESS_JAVA, Monitor.VALUE | Monitor.ALARM, false);
        PVManager.setDefaultDataSource(jca);
        final AtomicInteger count = new AtomicInteger();
        
        PV<?> pv = PVManager.read(channel("counter1")).atHz(50);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                count.incrementAndGet();
            }
        });
        
        pv = PVManager.read(channel("counter1")).atHz(50);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                count.incrementAndGet();
            }
        });
        
        while (true) {
            Thread.sleep(1000);
        }
    }
}

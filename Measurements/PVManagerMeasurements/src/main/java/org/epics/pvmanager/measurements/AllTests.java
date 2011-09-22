/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.measurements;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;

/**
 *
 * @author carcassi
 */
public class AllTests {
    public static void main(String[] args) {
        PVManager.setDefaultDataSource(new JCADataSource());
        ConnectionDelay.main(new String[] {"counter1"});
        ConnectionDelay.main(new String[] {"counter2", "counter3", "counter4",
            "counter5", "counter6", "counter7", "counter8", "counter9", "counter10"});
        PVManager.getDefaultDataSource().close();
    }
}

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
    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        String[] channels20 = new String[] {"counter1", "counter2", "counter3", "counter4",
            "counter5", "counter6", "counter7", "counter8", "counter9", "counter10",
            "counter11", "counter12", "counter13", "counter14",
            "counter15", "counter16", "counter17", "counter18", "counter19", "counter20"};
        String[] channels10 = new String[] {"counter1", "counter2", "counter3", "counter4",
            "counter5", "counter6", "counter7", "counter8", "counter9", "counter10",
            "counter11", "counter12", "counter13", "counter14",
            "counter15", "counter16", "counter17", "counter18", "counter19", "counter20"};
        String[] channels = channels20;
        //ConnectionDelay.main(new String[] {"counter1"});
//        ConnectionDelay.main(new String[] {"counter1", "counter2", "counter3", "counter4",
//            "counter5", "counter6", "counter7", "counter8", "counter9", "counter10"});
        JCAMassConnectionDelay.main(channels);
        Thread.sleep(1000);
        ConsecutiveConnectionDelay.main(channels);
        Thread.sleep(1000);
        JCAMassConnectionDelay.main(channels);
        Thread.sleep(1000);
        ConsecutiveConnectionDelay.main(channels);
        Thread.sleep(1000);
        JCAMassConnectionDelay.main(channels);
        Thread.sleep(1000);
        ConsecutiveConnectionDelay.main(channels);
        //PVManager.getDefaultDataSource().close();
        System.exit(0);
    }
}

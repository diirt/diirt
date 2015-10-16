/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.measurements;

import gov.bnl.channelfinder.api.Channel;
import gov.bnl.channelfinder.api.ChannelFinderClient;
import gov.bnl.channelfinder.api.ChannelFinderClientImpl.CFCBuilder;
import gov.bnl.channelfinder.api.ChannelUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;

/**
 *
 * @author carcassi
 */
public class AllTests {
    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        ChannelFinderClient cf = CFCBuilder.serviceURL("http://channelfinder.nsls2.bnl.gov").create();
        Collection<Channel> queriedChannels = cf.find("Tags=aphla.sys.SR");
        Collection<String> channelNames = ChannelUtil.getChannelNames(queriedChannels);
        System.out.println(channelNames.size());

        String[] channels20 = new String[] {"counter1", "counter2", "counter3", "counter4",
            "counter5", "counter6", "counter7", "counter8", "counter9", "counter10",
            "counter11", "counter12", "counter13", "counter14",
            "counter15", "counter16", "counter17", "counter18", "counter19", "counter20"};
        String[] channels10 = new String[] {"counter1", "counter2", "counter3", "counter4",
            "counter5", "counter6", "counter7", "counter8", "counter9", "counter10",
            "counter11", "counter12", "counter13", "counter14",
            "counter15", "counter16", "counter17", "counter18", "counter19", "counter20"};
        List<String> channelList = new ArrayList<String>();
        String[] channels = channelNames.toArray(new String[channelNames.size()]);
        //ConnectionDelay.main(new String[] {"counter1"});
//        ConnectionDelay.main(new String[] {"counter1", "counter2", "counter3", "counter4",
//            "counter5", "counter6", "counter7", "counter8", "counter9", "counter10"});
        //JCAMassConnectionDelay.main(channels);
        Thread.sleep(1000);
        ListConnectionDelay.main(channels);
        Thread.sleep(1000);
        JCAMassConnectionDelay.main(channels);
        Thread.sleep(1000);
        ListConnectionDelay.main(channels);
        Thread.sleep(1000);
        JCAMassConnectionDelay.main(channels);
        Thread.sleep(1000);
        ListConnectionDelay.main(channels);
        Thread.sleep(1000);
        JCAMassConnectionDelay.main(channels);
        Thread.sleep(1000);
        ListConnectionDelay.main(channels);
        Thread.sleep(1000);
        JCAMassConnectionDelay.main(channels);
        Thread.sleep(1000);
        ListConnectionDelay.main(channels);
        //PVManager.getDefaultDataSource().close();
        System.exit(0);
    }
}

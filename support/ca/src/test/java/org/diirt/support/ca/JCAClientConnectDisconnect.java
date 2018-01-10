/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReaderListener;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PVReaderEvent;
import static org.diirt.util.time.TimeDuration.*;

/**
 * This class tests opening and closing the same channels multiple times
 * to check whether any of the initial events are lost. This test is designed
 * to quickly check if there are problems, and not to help debug them.
 * <p>
 * For the test to be useful, it's better that the channels are not changing.
 * In that case, the number of connections has to be equal to the number of
 * connection notifications and value notifications. The total notifications
 * can be greater than the number of connections because connection and value
 * notifications can come in the same or in two different events.
 * <p>
 * The test will keep connecting and disconnecting a random channel. The same
 * channel is used twice, so that in some cases the channel is opened from
 * scratch and in other cases is opened while already opened. This will test
 * both the JCA connection logic and the connection sharing logic.
 *
 * @author carcassi
 */
public class JCAClientConnectDisconnect {
    public static void main(String[] args) throws Exception {
        JCADataSource jca = new JCADataSourceProvider().createInstance();
        PVManager.setDefaultDataSource(jca);

        String channelNamePrefix = "TEST_";

        List<String> names = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            names.add(channelNamePrefix + i);
            names.add(channelNamePrefix + i);
        }
        List<PVReader<?>> pvs = new ArrayList<PVReader<?>>();
        for (String name : names) {
            pvs.add(null);
        }
        Random rand = new Random(1);
        final AtomicInteger connectCount = new AtomicInteger(0);
        final AtomicInteger valueNotificationCount = new AtomicInteger(0);
        final AtomicInteger connectionNotificationCount = new AtomicInteger(0);
        final AtomicInteger totalNotificationCount = new AtomicInteger(0);

        int countToPrint = 10;
        while (true) {

            int index = rand.nextInt(names.size());
            PVReader<?> pv = pvs.get(index);
            if (pv == null) {
                pv = PVManager.read(channel(names.get(index)))
                        .readListener(new PVReaderListener<Object>() {
                            @Override
                            public void pvChanged(PVReaderEvent<Object> event) {
                                if (event.isConnectionChanged()) {
                                    connectionNotificationCount.incrementAndGet();
                                }
                                if (event.isValueChanged()) {
                                    valueNotificationCount.incrementAndGet();
                                }
                                totalNotificationCount.incrementAndGet();
                            }
                        })
                        .maxRate(ofHertz(100));
                pvs.set(index, pv);
                connectCount.incrementAndGet();
            } else {
                pv.close();
                pvs.set(index, null);
            }

            Thread.sleep(50);
            if (connectCount.get() == countToPrint) {
                System.out.println("Connections: " + connectCount + " Conn notifications: " + connectionNotificationCount
                        + " Value notifications: " + valueNotificationCount + " Total notifications: " +totalNotificationCount);
                countToPrint += 10;
            }
        }
    }
}

package org.epics.pvmanager.measurements;

import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.Monitor;
import gov.aps.jca.dbr.DBRType;
import gov.aps.jca.event.ConnectionEvent;
import gov.aps.jca.event.ConnectionListener;
import gov.aps.jca.event.GetEvent;
import gov.aps.jca.event.GetListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;

/**
 * Hello world!
 *
 */
public class JCAMassConnectionDelay
{
    public static void main( String[] args ) throws Exception {
        final StopWatch watch = new StopWatch();
        final CountDownLatch latch = new CountDownLatch(args.length * 2);
        watch.start();
        final List<Channel> channels = new ArrayList<Channel>();
        for (String string : args) {
            JCADataSource source = (JCADataSource) PVManager.getDefaultDataSource();
            final Context context = source.getContext();
            Channel channel = context.createChannel(string, new ConnectionListener() {

                @Override
                public void connectionChanged(ConnectionEvent ev) {
                    try {
                    Channel channel = (Channel) ev.getSource();
                    channel.get(DBRType.CTRL_DOUBLE, 1, new GetListener() {

                        @Override
                        public void getCompleted(GetEvent ev) {
                            if (ev.getDBR().hashCode() == 12345678) {
                                System.out.println("Ping");
                            }
                            latch.countDown();
                        }
                    });
                    channel.addMonitor(DBRType.TIME_DOUBLE, 1, Monitor.VALUE, new MonitorListener() {

                            @Override
                            public void monitorChanged(MonitorEvent ev) {
                                if (ev.getDBR().hashCode() == 12345678) {
                                    System.out.println("Ping");
                                }
                                latch.countDown();
                            }
                        });
                    context.flushIO();
                    } catch (Exception ex) {

                    }
                }
            });
            channels.add(channel);
        }
        latch.await();
        watch.stop();
        for (Channel channel : channels) {
            channel.dispose();
        }
        watch.printLast(System.out);
    }
}

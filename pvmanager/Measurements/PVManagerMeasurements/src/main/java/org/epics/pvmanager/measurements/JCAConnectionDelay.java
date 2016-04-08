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
import java.util.concurrent.CountDownLatch;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.util.TimeDuration.*;

/**
 * Hello world!
 *
 */
public class JCAConnectionDelay
{
    public static void main( String[] args ) throws Exception {
        final StopWatch watch = new StopWatch();
        for (String string : args) {
            final CountDownLatch latch = new CountDownLatch(2);
            watch.start();
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
            try {
                latch.await();
            } catch (InterruptedException ex) {
            }
            watch.stop();
            channel.dispose();
        }
        watch.printStatisticsMS(System.out);
    }
}

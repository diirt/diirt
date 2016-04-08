package org.epics.pvmanager.measurements;

import java.util.concurrent.CountDownLatch;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderListener;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.util.TimeDuration.*;

/**
 * Hello world!
 *
 */
public class ConnectionDelay
{
    public static void main( String[] args ) {
        final StopWatch watch = new StopWatch();
        for (String string : args) {
            final CountDownLatch latch = new CountDownLatch(1);
            watch.start();
            final PVReader reader = PVManager.read(channel(string)).every(ms(5));
            reader.addPVReaderListener(new PVReaderListener() {

                public void pvChanged() {
                    watch.stop();
                    latch.countDown();
                    reader.close();
                }
            });
            try {
                latch.await();
            } catch (InterruptedException ex) {

            }
        }
        watch.printStatisticsMS(System.out);
    }
}

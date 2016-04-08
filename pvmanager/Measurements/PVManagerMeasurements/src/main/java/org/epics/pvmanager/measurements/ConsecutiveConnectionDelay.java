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
public class ConsecutiveConnectionDelay
{
    public static void main( String[] args ) {
        final StopWatch watch = new StopWatch();
        final CountDownLatch latch = new CountDownLatch(args.length);
        watch.start();
        for (String string : args) {
            final PVReader reader = PVManager.read(channel(string)).every(ms(5));
            reader.addPVReaderListener(new PVReaderListener() {

                public void pvChanged() {
                    latch.countDown();
                    reader.close();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException ex) {

        }
        watch.stop();

        watch.printLast(System.out);
    }
}

package org.epics.pvmanager.measurements;

import java.util.List;
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
public class ListConnectionDelay
{
    public static void main( String[] args ) {
        final StopWatch watch = new StopWatch();
        final CountDownLatch latch = new CountDownLatch(1);
        watch.start();
        final PVReader<List<Object>> reader = PVManager.read(listOf(latestValueOf(channels(args)))).every(ms(5));
        reader.addPVReaderListener(new PVReaderListener() {

            public void pvChanged() {
                boolean allIn = true;
                for (Object obj : reader.getValue()) {
                    if (obj == null)
                        allIn = false;
                }
                if (allIn) {
                    latch.countDown();
                    reader.close();
                }
            }
        });
        try {
            latch.await();
        } catch (InterruptedException ex) {

        }
        watch.stop();

        watch.printLast(System.out);
    }
}

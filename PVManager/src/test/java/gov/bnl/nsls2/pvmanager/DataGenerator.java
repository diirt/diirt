/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author carcassi
 */
public class DataGenerator {

    private static Random rand = new Random();

    public static void generateData(final Collector collector, final int nTimes, long period, final int samplesPerPeriod) {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int innerCounter;

            @Override
            public void run() {
                for (int i = 0; i < samplesPerPeriod; i++) {
                    collector.post(rand.nextGaussian());
                }
                innerCounter++;
                if (innerCounter == nTimes) {
                    timer.cancel();
                }
            }
        }, 0, period);
    }
}

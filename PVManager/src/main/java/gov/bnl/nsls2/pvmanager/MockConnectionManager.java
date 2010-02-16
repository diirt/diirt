/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author carcassi
 */
public class MockConnectionManager {
    static MockConnectionManager instance = new MockConnectionManager();

    private static Random rand = new Random();

    private static void generateData(final Collector collector, final TypeDouble value, final int nTimes, long period, final int samplesPerPeriod) {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int innerCounter;

            @Override
            public void run() {
                for (int i = 0; i < samplesPerPeriod; i++) {
                    synchronized (collector) {
                        value.setDouble(rand.nextGaussian());
                        collector.collect();
                    }
                }
                innerCounter++;
                if (innerCounter == nTimes) {
                    timer.cancel();
                }
            }
        }, 0, period);
    }

    Pattern pattern = Pattern.compile("(\\d*)samples_every(\\d*)ms_for(\\d*)times");

    private void connect(String name, Collector collector, TypeDouble typeDouble) {
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            int nTimes = Integer.parseInt(matcher.group(3));
            long period = Long.parseLong(matcher.group(2));
            int samplesPerPeriod = Integer.parseInt(matcher.group(1));
            generateData(collector, typeDouble, nTimes, period, samplesPerPeriod);
        } else {
            throw new RuntimeException("Name doesn't match for mock connection");
        }
    }

    public static String mockPVName(int samplesPerNotification, long notificationPeriodMs, int nNotifications) {
        return "" + samplesPerNotification + "samples_every" + notificationPeriodMs + "ms_for" + nNotifications + "times";
    }

    void monitor(MonitorRecipe<TypeDouble> connRecipe) {
        connect(connRecipe.pvName, connRecipe.collector, connRecipe.cache.getValue());
    }
}

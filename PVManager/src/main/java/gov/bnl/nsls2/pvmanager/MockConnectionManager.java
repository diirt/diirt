/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author carcassi
 */
class MockConnectionManager extends ConnectionManager {

    private static Logger log = Logger.getLogger(ConnectionManager.class.getName());
    static MockConnectionManager instance = new MockConnectionManager();

    private static Random rand = new Random();

    private static Timer timer = new Timer("Mock Data Generator", true);

    private static void generateData(final Collector collector, final TypeDouble value, final int nTimes, long period, final int samplesPerPeriod) {
        timer.scheduleAtFixedRate(new TimerTask() {
            int innerCounter;
            WeakReference<Collector> ref = new WeakReference<Collector>(collector);

            @Override
            public void run() {
                Collector collector = ref.get();
                if (collector != null) {
                    for (int i = 0; i < samplesPerPeriod; i++) {
                        synchronized (collector) {
                            value.setDouble(rand.nextGaussian());
                            collector.collect();
                        }
                    }
                }
                innerCounter++;
                if (innerCounter == nTimes || collector == null) {
                    log.fine("Stopped generating data on " + collector);
                    cancel();
                }
            }
        }, 0, period);
        log.fine("Generating data on " + collector);
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

    @Override
    void monitor(MonitorRecipe recipe) {
        if (recipe.cache.getType().equals(TypeDouble.class)) {
            connect(recipe.pvName, recipe.collector, TypeDouble.class.cast(recipe.cache.getValue()));
        } else {
            throw new UnsupportedOperationException("Type " + recipe.cache.getType().getName() + " is not yet supported");
        }
    }

    @Override
    void connect(ConnectionRecipe connRecipe) {
        // Do nothing for now
    }

}

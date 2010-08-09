/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.ValueFactory;

/**
 * Simulated function for a ramp.
 *
 * @author carcassi
 */
class Ramp {

    private double min;
    private double max;
    private double currentValue;
    private double step;
    private double range;
    private double interval;
    private VDouble lastValue;

    public Ramp(Double min, Double max, Double step, Double interval) {
        if (interval <= 0.0) {
            throw new IllegalArgumentException("Interval must be greater than zero (was " + interval + ")");
        }
        this.min = min;
        this.max = max;
        this.currentValue = min - step;
        this.step = step;
        this.interval = interval;
        range = max - min;
        lastValue = ValueFactory.newVDouble(currentValue, AlarmSeverity.NONE, Collections.<String>emptySet(),
                Constants.POSSIBLE_ALARM_STATUS, TimeStamp.now(), null,
                min, min + range * 0.1, min + range * 0.2, "x", Constants.DOUBLE_FORMAT,
                min + range * 0.8, min + range * 0.9, max, min, max);
    }

    private AlarmSeverity alarmSeverity() {
        double position = (currentValue - min) / range;
        if (position <= 0.1 || position >= 0.9)
            return AlarmSeverity.MAJOR;
        if (position <= 0.2 || position >= 0.8)
            return AlarmSeverity.MINOR;
        return AlarmSeverity.NONE;
    }

    public VDouble nextVDouble() {
        currentValue = currentValue + step;
        if (currentValue > max) {
            currentValue = min;
        }

        return ValueFactory.newVDouble(currentValue, alarmSeverity(), Constants.NO_ALARMS,
                null, TimeStamp.now(), lastValue);
    }
    
    private TimerTask task;

    public void start(Timer timer, final Collector collector, final ValueCache<VDouble> cache) {
        if (task != null)
            task.cancel();
        task = new TimerTask() {
            int innerCounter;
            SimulationDataSource.ValueProcessor<Object, VDouble> processor = new SimulationDataSource.ValueProcessor<Object, VDouble>(collector, cache) {

                @Override
                public void close() {
                    cancel();
                }

                @Override
                public boolean updateCache(Object payload, ValueCache<VDouble> cache) {
                    cache.setValue(nextVDouble());
                    return true;
                }
            };

            @Override
            public void run() {
                processor.processValue(null);
//                for (int i = 0; i < samplesPerPeriod; i++) {
//                    processor.processValue(null);
//                }
//                innerCounter++;
//                if (innerCounter == nTimes) {
//                    log.fine("Stopped generating data on " + collector);
//                    processor.close();
//                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, (long) (interval * 1000));
    }
}

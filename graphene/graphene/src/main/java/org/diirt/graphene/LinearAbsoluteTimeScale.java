/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.util.List;

import org.diirt.util.array.ArrayDouble;

import java.time.Duration;

import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * A time scale where absolute time is used linearly.
 *
 * @author carcassi
 */
final class LinearAbsoluteTimeScale implements TimeScale {

    @Override
    public double scaleNormalizedTime(double value, double newMinValue, double newMaxValue) {
        double newRange = newMaxValue - newMinValue;
        return newMinValue + (value) * newRange;
    }

    @Override
    public double scaleTimestamp(Instant value, TimeInterval timeInterval, double newMinValue, double newMaxValue) {
        double fromStart =  timeInterval.getStart().until(value, ChronoUnit.SECONDS);
        double range = timeInterval.getStart().until(timeInterval.getEnd(), ChronoUnit.SECONDS);
        double newRange = newMaxValue - newMinValue;
        return newMinValue + (fromStart) / range * newRange;
    }

    @Override
    public TimeAxis references(TimeInterval range, int minRefs, int maxRefs) {
        // Validate input. Make sure requested references range makes sense.
        if ( (maxRefs < minRefs) || (minRefs < 0 ) || (maxRefs < 0) ) {
            throw new IllegalArgumentException( "Invalid references range: " + minRefs + " < # references < " + maxRefs );
        }

        // First guess at the time between references.
        // Get the smallest required period, and then round down

        double minPeriodInSec;
        if( maxRefs == 0){
            minPeriodInSec = 0.0;
        }else{
            minPeriodInSec = range.getStart().until(range.getEnd(), ChronoUnit.SECONDS) / maxRefs;
        }
        TimeScales.TimePeriod timePeriod = TimeScales.toTimePeriod(minPeriodInSec);
        timePeriod = TimeScales.nextDown(timePeriod);

        // Keep increasing the time until you have the right amount of references
        List<Instant> references = TimeScales.createReferences(range, timePeriod);
        while(references.size() > maxRefs) {
            timePeriod = TimeScales.nextUp(timePeriod);
            references = TimeScales.createReferences(range, timePeriod);
        }
        if (references.size() < minRefs) {
            throw new RuntimeException("Can't create the requested amount of references. Could only create: " + references.size() + ", minimum required: " + minRefs );
        }

        // Prepare normalized values
        double[] normalized = new double[references.size()];
        for (int i = 0; i < references.size(); i++) {
            normalized[i] = TimeScales.normalize(references.get(i), range);
        }
        ArrayDouble normalizedValues = new ArrayDouble(normalized);

        return new TimeAxis(range, references, normalizedValues, TimeScales.trimLabels(TimeScales.createLabels(references)));
    }

}

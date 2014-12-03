/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.util.List;
import org.epics.util.array.ArrayDouble;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;

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
    public double scaleTimestamp(Timestamp value, TimeInterval timeInterval, double newMinValue, double newMaxValue) {
        double fromStart = value.durationFrom(timeInterval.getStart()).toSeconds();
        double range = timeInterval.getEnd().durationFrom(timeInterval.getStart()).toSeconds();
        double newRange = newMaxValue - newMinValue;
        return newMinValue + (fromStart) / range * newRange;
    }

    @Override
    public TimeAxis references(TimeInterval range, int minRefs, int maxRefs) {
	
	//although it's the user's responsibility to check for improper inputs,
	//we should check for them anyways.
	//If the maximum references is less than the minimum references,
	//or the maxium references is less than 0, then this method
	//can enter an infinite loop, which we want to avoid, 
	//so we should make an exception
	if ( (maxRefs < minRefs) || (minRefs < 0 ) || (maxRefs < 0) ) {
	    throw new IllegalArgumentException( "Invalid references range: " + minRefs + " < # references < " + maxRefs );
	}
        // First guess at the time between references.
        // Get the smallest required period, and then round down
        TimeDuration rangeDuration = range.getEnd().durationFrom(range.getStart());
        double minPeriodInSec = rangeDuration.toSeconds() / maxRefs;
        TimeScales.TimePeriod timePeriod = TimeScales.toTimePeriod(minPeriodInSec);
        timePeriod = TimeScales.nextDown(timePeriod);
        
        // Keep increasing the time until you have the right amount of references
        List<Timestamp> references = TimeScales.createReferences(range, timePeriod);
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

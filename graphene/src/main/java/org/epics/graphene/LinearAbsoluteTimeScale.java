/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.GregorianCalendar;
import java.util.List;
import static org.epics.graphene.ValueAxis.orderOfMagnitude;
import org.epics.util.array.ArrayDouble;
import org.epics.util.text.NumberFormats;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;

/**
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
        // First guess at the time between references.
        // Get the smallest required period, and then round down
        TimeDuration rangeDuration = range.getEnd().durationFrom(range.getStart());
        double minPeriodInSec = rangeDuration.toSeconds() / maxRefs;
        TimeScales.TimePeriod timePeriod = TimeScales.toTimePeriod(minPeriodInSec);
        timePeriod = TimeScales.nextDown(timePeriod);
        
        List<Timestamp> references = TimeScales.createReferences(range, timePeriod);
        while(references.size() > maxRefs) {
            timePeriod = TimeScales.nextUp(timePeriod);
            references = TimeScales.createReferences(range, timePeriod);
        }
        double[] normalized = new double[references.size()];
        for (int i = 0; i < references.size(); i++) {
            normalized[i] = TimeScales.normalize(references.get(i), range);
        }
        ArrayDouble normalizedValues = new ArrayDouble(normalized);
        
        return new TimeAxis(range, references, normalizedValues, TimeScales.trimLabelsLeft(TimeScales.trimLabelsRight(TimeScales.createLabels(references))));
    }
    
}

/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.AlarmStatus;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import java.util.List;
import org.diirt.datasource.Aggregator;
import org.diirt.datasource.ReadFunction;
import static org.epics.vtype.AlarmSeverity.*;

/**
 * Aggregates the values by taking the average.
 *
 * @author carcassi
 */
class AverageAggregator extends Aggregator<VDouble, VDouble> {

    AverageAggregator(ReadFunction<List<VDouble>> collector) {
        super(collector);
    }

    @Override
    protected VDouble calculate(List<VDouble> data) {
        // TODO: this code should be consolidated with the StatisticsDoubleAggregator
        double totalSum = 0;
        AlarmSeverity statSeverity = null;
        for (VDouble vDouble : data) {
            switch(vDouble.getAlarm().getSeverity()) {
                case NONE:
                    // if severity was never MINOR or MAJOR,
                    // severity should be NONE
                    if (statSeverity != MINOR || statSeverity != MAJOR)
                        statSeverity = NONE;
                    totalSum += vDouble.getValue();
                    break;

                case MINOR:
                    // If severity was never MAJOR,
                    // set it to MINOR
                    if (statSeverity != MAJOR)
                        statSeverity = MINOR;
                    totalSum += vDouble.getValue();
                    break;

                case MAJOR:
                    statSeverity = MAJOR;
                    totalSum += vDouble.getValue();
                    break;

                case UNDEFINED:
                    if (statSeverity == null)
                        statSeverity = UNDEFINED;
                    break;

                case INVALID:
                    if (statSeverity == null || statSeverity == UNDEFINED)
                        statSeverity = INVALID;
                    break;

                default:
            }
        }
        return VDouble.of(totalSum / data.size(),
                Alarm.of(statSeverity, AlarmStatus.NONE ,"NONE"),
                Time.of(data.get(data.size() / 2).getTime().getTimestamp()),
                data.get(0).getDisplay());
    }

}

/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.dbr.TIME;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmProvider;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.AlarmStatus;
import org.epics.vtype.Time;
import org.epics.vtype.TimeProvider;

import java.time.Instant;

/**
 *
 * @author carcassi
 */
class VMetadata<TValue extends TIME> implements AlarmProvider, TimeProvider {

    final TValue dbrValue;
    private final boolean disconnected;
    private final Instant timestamp;

    VMetadata(TValue dbrValue, JCAConnectionPayload connPayload) {
        this.dbrValue = dbrValue;
        this.disconnected = !connPayload.isChannelConnected();
        if (disconnected) {
            timestamp = connPayload.getEventTime();
        } else {
            timestamp = DataUtils.timestampOf(dbrValue.getTimeStamp());
        }
    }

    public AlarmSeverity getAlarmSeverity() {
        if (disconnected)
            return AlarmSeverity.UNDEFINED;
        return DataUtils.alarmSeverityFromEpics(dbrValue.getSeverity());
    }

    public AlarmStatus getAlarmStatus() {
        if (disconnected)
            return AlarmStatus.UNDEFINED;
        return DataUtils.alarmStatusfromEpics(dbrValue.getStatus());
    }

    public String getAlarmName() {
        if (disconnected)
            return "Disconnected";
        return dbrValue.getStatus().getName();
    }

    public Integer getTimeUserTag() {
        return null;
    }

    public boolean isTimeValid() {
        return DataUtils.isTimeValid(dbrValue.getTimeStamp());
    }

    @Override
    public Time getTime() {
        return Time.of(timestamp, getTimeUserTag(), isTimeValid());
    }

    @Override
    public Alarm getAlarm() {
        return Alarm.of(getAlarmSeverity(), getAlarmStatus(), getAlarmName());
    }

}

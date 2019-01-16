/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import javax.xml.bind.annotation.XmlAttribute;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmProvider;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Time;
import org.epics.vtype.TimeProvider;

/**
 *
 * @author carcassi
 */
class XmlVMetaData extends ReplayValue implements TimeProvider, AlarmProvider {

    @XmlAttribute
    Integer timeUserTag;
    @XmlAttribute
    AlarmSeverity alarmSeverity;
    @XmlAttribute
    String alarmName;

    public Integer getTimeUserTag() {
        return timeUserTag;
    }

    public AlarmSeverity getAlarmSeverity() {
        return alarmSeverity;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public boolean isTimeValid() {
        return true;
    }

    @Override
    public Alarm getAlarm() {
        return Alarm.of(getAlarmSeverity(), null, getAlarmName());
    }

    @Override
    public Time getTime() {
        return Time.of(getTimestamp());
    }

}

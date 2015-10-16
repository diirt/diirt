/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.adapters;

import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVStructure;
import org.diirt.util.time.Timestamp;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.Time;
import org.diirt.vtype.ValueFactory;

public class AlarmTimeExtractor implements Alarm, Time {

        protected final AlarmSeverity alarmSeverity;
        protected final String alarmStatus;
        protected final Timestamp timeStamp;
        protected final Integer timeUserTag;
        protected final boolean isTimeValid;

        private static final Alarm noAlarm = ValueFactory.alarmNone();

        private static final Timestamp noTimeStamp = org.diirt.util.time.Timestamp.of(0,0);
        private static final Integer noTimeUserTag = null;

        public AlarmTimeExtractor(PVStructure pvField, boolean disconnected)
        {
                // alarm_t
                if (disconnected)
                {
                        alarmSeverity = AlarmSeverity.UNDEFINED;
                        alarmStatus = "DISCONNECTED";
                }
                else
                {
                        PVStructure alarmStructure = (pvField != null) ? pvField.getStructureField("alarm") : null;
                        if (alarmStructure != null)
                        {
                                PVInt severityField = alarmStructure.getIntField("severity");
                                if (severityField == null)
                                        alarmSeverity = AlarmSeverity.UNDEFINED;
                                else
                                        alarmSeverity = alarmSeverityMapLUT[severityField.get()];
                                // no explicit out-of-bounds check


                                PVInt statusField = alarmStructure.getIntField("status");
                                if (statusField == null)
                                        alarmStatus = "UNDEFINED";
                                else
                                        alarmStatus = alarmStatusMapLUT[statusField.get()];
                                // no explicit out-of-bounds check

                        }
                        else
                        {
                                alarmSeverity = noAlarm.getAlarmSeverity();
                                alarmStatus = noAlarm.getAlarmName();
                        }
                }

                // timeStamp_t
                PVStructure timeStampStructure = (pvField != null) ? pvField.getStructureField("timeStamp") : null;
                if (timeStampStructure != null)
                {
                        PVLong secsField = timeStampStructure.getLongField("secondsPastEpoch");
                        PVInt nanosField = timeStampStructure.getIntField("nanoseconds");

                        if (secsField == null || nanosField == null)
                                timeStamp = noTimeStamp;
                        else
                                timeStamp = org.diirt.util.time.Timestamp.of(secsField.get(), nanosField.get());

                        PVInt userTagField = timeStampStructure.getIntField("userTag");
                        if (userTagField == null)
                                timeUserTag = noTimeUserTag;
                        else
                                timeUserTag = userTagField.get();

                        isTimeValid = (timeStamp != noTimeStamp);
                }
                else
                {
                        timeStamp = org.diirt.util.time.Timestamp.now();
                        timeUserTag = null;
                        isTimeValid = true;
                }

        }

        // org.epics.pvdata.property.AlarmSeverity to pvmanager.AlarmSeverity
        protected static final AlarmSeverity alarmSeverityMapLUT[] =
        {
                AlarmSeverity.NONE,
                AlarmSeverity.MINOR,
                AlarmSeverity.MAJOR,
                AlarmSeverity.INVALID,
                AlarmSeverity.UNDEFINED
        };

        // org.epics.pvdata.property.AlarmStatus to pvmanager.AlarmStatus
        protected static final String alarmStatusMapLUT[] =
        {
                "NONE",
                "DEVICE",
                "DRIVER",
                "RECORD",
                "DB",
                "CONF",
                "UNDEFINED",
                "CLIENT"
        };

        @Override
        public AlarmSeverity getAlarmSeverity() {
                return alarmSeverity;
        }

    @Override
    public String getAlarmName() {
        return alarmStatus;
    }



        @Override
        public Timestamp getTimestamp() {
                return timeStamp;
        }

        @Override
        public Integer getTimeUserTag() {
                return timeUserTag;
        }

        @Override
        public boolean isTimeValid() {
                return isTimeValid;
        }

}

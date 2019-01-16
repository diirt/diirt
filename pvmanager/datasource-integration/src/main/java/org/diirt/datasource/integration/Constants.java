/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import java.time.Instant;
import java.util.Arrays;

import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.EnumDisplay;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VEnum;
import org.epics.vtype.VInt;
import org.epics.vtype.VString;

/**
 *
 * @author carcassi
 */
public class Constants {
    public static final String const_double = "const-double";
    public static final VDouble const_double_value = VDouble.of(0.13,
            Alarm.of(AlarmSeverity.INVALID, null, "UDF_ALARM"),
            Time.of(Instant.ofEpochSecond(631152000, 0), null, false), Display.none());
    public static final String const_int = "const-i32";
    public static final VInt const_int_value = VInt.of(0, Alarm.of(AlarmSeverity.INVALID, null, "UDF_ALARM"),
            Time.of(Instant.ofEpochSecond(631152000, 0), null, false), Display.none());
    public static final String const_string = "const-double.NAME";
    public static final VString const_string_value = VString.of("const-double",
            Alarm.of(AlarmSeverity.INVALID, null, "UDF_ALARM"),
            Time.of(Instant.ofEpochSecond(631152000, 0), null, false));
    public static final String const_enum = "const-double.SCAN";
    public static final VEnum const_enum_value = VEnum.of(0,
            EnumDisplay.of(Arrays.asList("Passive", "Event", "I/O Intr", "10 second", "5 second", "2 second",
                    "1 second", ".5 second", ".2 second", ".1 second")),
            Alarm.of(AlarmSeverity.INVALID, null, "UDF_ALARM"),
            Time.of(Instant.ofEpochSecond(631152000, 0), null, false));
    public static final String counter_double_1Hz = "double-counter-1Hz";
    public static final String counter_double_100Hz = "double-counter-100Hz";
    public static final String alarm_string = "TST:Alarm:String";
    public static final VString alarm_string_value = VString.of("Hello", Alarm.of(AlarmSeverity.NONE, null, "NO_ALARM"),
            Time.now());
}

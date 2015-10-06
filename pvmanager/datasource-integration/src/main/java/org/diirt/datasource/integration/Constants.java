/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import java.time.Instant;
import java.util.Arrays;

import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VString;

import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class Constants {
    public static final String const_double = "const-double";
    public static final VDouble const_double_value = newVDouble(0.13, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Instant.ofEpochSecond(631152000, 0), null, false), displayNone());
    public static final String const_int = "const-i32";
    public static final VInt const_int_value = newVInt(0, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Instant.ofEpochSecond(631152000, 0), null, false), displayNone());
    public static final String const_string = "const-double.NAME";
    public static final VString const_string_value = newVString("const-double", newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Instant.ofEpochSecond(631152000, 0), null, false));
    public static final String const_enum = "const-double.SCAN";
    public static final VEnum const_enum_value = newVEnum(0, Arrays.asList("Passive", "Event", "I/O Intr", "10 second", "5 second", "2 second", "1 second", ".5 second", ".2 second", ".1 second"), newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Instant.ofEpochSecond(631152000, 0), null, false));
    public static final String counter_double_1Hz = "double-counter-1Hz";
    public static final String counter_double_100Hz = "double-counter-100Hz";
    public static final String alarm_string = "TST:Alarm:String";
    public static final VString alarm_string_value = newVString("Hello", newAlarm(AlarmSeverity.NONE, "NO_ALARM"), timeNow());
}

/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import java.util.Arrays;
import org.epics.util.time.Timestamp;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.VDouble;
import org.epics.vtype.VEnum;
import org.epics.vtype.VString;
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class Constants {
    public static final String const_double = "const-double";
    public static final VDouble const_double_value = newVDouble(0.13, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone());
    public static final String const_string = "const-double.NAME";
    public static final VString const_string_value = newVString("const-double", newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false));
    public static final String const_enum = "const-double.SCAN";
    public static final VEnum const_enum_value = newVEnum(0, Arrays.asList("Passive", "Event", "I/O Intr", "10 second", "5 second", "2 second", "1 second", ".5 second", ".2 second", ".1 second"), newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false));
    public static final String counter_double_1Hz = "double-counter-1Hz";
    public static final String counter_double_100Hz = "double-counter-100Hz";
}

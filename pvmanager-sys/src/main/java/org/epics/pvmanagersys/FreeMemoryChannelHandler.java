/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanagersys;

import org.epics.util.text.NumberFormats;
import org.epics.vtype.Display;
import org.epics.vtype.DisplayBuilder;
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
class FreeMemoryChannelHandler extends SystemChannelHandler {

    public FreeMemoryChannelHandler(String channelName) {
        super(channelName);
    }
    
    private Display memoryDisplay = new DisplayBuilder().format(NumberFormats.toStringFormat())
            .units("byte")
            .lowerAlarmLimit(0.0).lowerCtrlLimit(0.0).lowerDisplayLimit(0.0).lowerWarningLimit(0.0)
            .upperAlarmLimit((double) Runtime.getRuntime().maxMemory())
            .upperCtrlLimit((double) Runtime.getRuntime().maxMemory())
            .upperDisplayLimit((double) Runtime.getRuntime().maxMemory())
            .upperWarningLimit((double) Runtime.getRuntime().maxMemory())
            .build();

    @Override
    protected Object createValue() {
        return newVInt((int) Runtime.getRuntime().freeMemory(), alarmNone(), timeNow(), memoryDisplay);
    }
    
}

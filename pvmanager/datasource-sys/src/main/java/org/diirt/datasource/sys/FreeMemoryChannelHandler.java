/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import org.epics.vtype.Alarm;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;

/**
 *
 * @author carcassi
 */
class FreeMemoryChannelHandler extends SystemChannelHandler {

    public FreeMemoryChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    protected Object createValue() {
        return VDouble.of(bytesToMebiByte(Runtime.getRuntime().freeMemory()), Alarm.none(), Time.now(), memoryDisplay);
    }

}

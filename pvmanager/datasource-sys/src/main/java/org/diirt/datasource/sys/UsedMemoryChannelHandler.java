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
class UsedMemoryChannelHandler extends SystemChannelHandler {

    public UsedMemoryChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    protected Object createValue() {
        return VDouble.of(bytesToMebiByte(Runtime.getRuntime().totalMemory()), Alarm.none(), Time.now(), memoryDisplay);
    }

}

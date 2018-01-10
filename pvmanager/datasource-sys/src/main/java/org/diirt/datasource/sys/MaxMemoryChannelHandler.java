/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
class MaxMemoryChannelHandler extends SystemChannelHandler {

    public MaxMemoryChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    protected Object createValue() {
        return newVDouble(bytesToMebiByte(Runtime.getRuntime().maxMemory()), alarmNone(), timeNow(), memoryDisplay);
    }

}

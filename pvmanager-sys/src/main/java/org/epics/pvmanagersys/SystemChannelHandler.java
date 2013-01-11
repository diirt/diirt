/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanagersys;

import org.epics.pvmanager.MultiplexedChannelHandler;
import org.epics.pvmanager.ChannelWriteCallback;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.*;
import org.epics.util.text.NumberFormats;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;
import org.epics.vtype.Display;
import org.epics.vtype.DisplayBuilder;

/**
 *
 * @author carcassi
 */
abstract class SystemChannelHandler extends MultiplexedChannelHandler<Object, Object> {
    
    protected Display memoryDisplay = new DisplayBuilder().format(NumberFormats.toStringFormat())
            .units("byte")
            .lowerAlarmLimit(0.0).lowerCtrlLimit(0.0).lowerDisplayLimit(0.0).lowerWarningLimit(0.0)
            .upperAlarmLimit((double) Runtime.getRuntime().maxMemory())
            .upperCtrlLimit((double) Runtime.getRuntime().maxMemory())
            .upperDisplayLimit((double) Runtime.getRuntime().maxMemory())
            .upperWarningLimit((double) Runtime.getRuntime().maxMemory())
            .build();

    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            // Protect the timer thread for possible problems.
            try {
                Object newValue = createValue();

                processMessage(newValue);
            } catch (Exception ex) {
                log.log(Level.WARNING, "Data simulation problem", ex);
            }
        }
    };
    private static final Logger log = Logger.getLogger(SystemChannelHandler.class.getName());
    private ScheduledFuture<?> taskFuture;
    
    protected abstract Object createValue();

    public SystemChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    public void connect() {
        taskFuture = SystemDataSource.getScheduledExecutorService().scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
        processConnection(new Object());
    }

    @Override
    public void disconnect() {
        taskFuture.cancel(false);
        taskFuture = null;
        processConnection(null);
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        throw new UnsupportedOperationException("Can't write to system channel.");
    }

    @Override
    public boolean isConnected(Object connection) {
        return taskFuture != null;
    }
}

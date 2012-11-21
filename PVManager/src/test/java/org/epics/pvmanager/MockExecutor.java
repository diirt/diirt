/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.Executor;

/**
 *
 * @author carcassi
 */
public class MockExecutor implements Executor {
    
    private volatile Runnable command;

    @Override
    public void execute(Runnable command) {
        this.command = command;
    }

    public Runnable getCommand() {
        return command;
    }
    
}

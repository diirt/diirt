/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

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

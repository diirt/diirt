/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

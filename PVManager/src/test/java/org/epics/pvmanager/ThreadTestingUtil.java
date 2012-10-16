/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

import java.util.concurrent.Callable;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class ThreadTestingUtil {
    
    /**
     * Waits until either the task returns a value or the timeout expires.
     * The task is repeated every ms.
     * 
     * @param <T> the return type of the task
     * @param task the task
     * @param timeout the timeout
     * @return the value from the task or null
     * @throws Exception an exception from the task
     */
    public static <T> T waitFor(Callable<T> task, TimeDuration timeout) 
    throws Exception {
        TimeInterval runInterval = timeout.after(Timestamp.now());
        while (runInterval.contains(Timestamp.now())) {
            T value = task.call();
            if (value != null) {
                return value;
            }
            Thread.sleep(1);
        }
        return null;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import javax.swing.SwingUtilities;

/**
 * Embeds the logic to post events on a seperate thread so that PVManager
 * can appropriately redirect the notifications.
 *
 * @author carcassi
 */
public abstract class ThreadSwitch {

    /**
     * Tells the PV manager to notify on the Swing Event Dispatch Thread using
     * SwingUtilities.invokeLater().
     * @return an object that posts events on the EDT
     */
    public static ThreadSwitch onSwingEDT() {
        return ThreadSwitch.SWING;
    }

    /**
     * Tells the PV manager to notify on the timer thread.
     * @return an object that runs tasks on the timer thread
     */
    public static ThreadSwitch onTimerThread() {
        return ThreadSwitch.TIMER;
    }

    /**
     * Post the given task to the notification thread.
     *
     * @param run a new task
     */
    public abstract void post(Runnable run);

    private static ThreadSwitch SWING = new ThreadSwitch() {

        @Override
        public void post(Runnable task) {
            SwingUtilities.invokeLater(task);
        }
    };

    private static ThreadSwitch TIMER = new ThreadSwitch() {

        @Override
        public void post(Runnable task) {
            task.run();
        }
    };

}

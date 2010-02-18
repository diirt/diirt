/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import javax.swing.SwingUtilities;

/**
 *
 * @author carcassi
 */
public abstract class ThreadSwitch {

    public abstract void post(Runnable run);

    static ThreadSwitch SWING = new ThreadSwitch() {

        @Override
        public void post(Runnable task) {
            SwingUtilities.invokeLater(task);
        }
    };

    static ThreadSwitch TIMER = new ThreadSwitch() {

        @Override
        public void post(Runnable task) {
            task.run();
        }
    };

}

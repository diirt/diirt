/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author carcassi
 */
public class Scanner {

    public static void scan(final PullNotificator notificator, long periodInMs) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                notificator.notifyPv();
            }
        }, 0, periodInMs);
    }
}

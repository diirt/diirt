/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
class Scanner {

    private static Logger log = Logger.getLogger(Scanner.class.getName());

    static void scan(final PullNotificator notificator, long periodInMs) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (notificator.isActive()) {
                    notificator.notifyPv();
                } else {
                    cancel();
                    log.fine("Stopped scanning " + notificator);
                }
            }
        }, 0, periodInMs);
        log.fine("Scanning " + notificator + " every " + periodInMs + " ms");
    }
}

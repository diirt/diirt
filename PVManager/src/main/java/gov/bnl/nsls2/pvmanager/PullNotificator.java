/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.lang.ref.WeakReference;
import javax.swing.SwingUtilities;

/**
 *
 * @author carcassi
 */
class PullNotificator<T extends PVType<T>> {

    private final WeakReference<PV<T>> pvRef;
    private final PVFunction<T> function;
    private final ThreadSwitch onThread;

    PullNotificator(PV<T> pv, PVFunction<T> aggregator, ThreadSwitch onThread) {
        this.pvRef = new WeakReference<PV<T>>(pv);
        this.function = aggregator;
        this.onThread = onThread;
    }

    boolean isActive() {
        if (pvRef.get() != null)
            return true;
        else
            return false;
    }

    void notifyPv() {
        final T newValue = function.getValue();
        onThread.post(new Runnable() {

            @Override
            public void run() {
                PV<T> pv = pvRef.get();
                if (pv != null) {
                    pv.getValue().setTo(newValue);
                }
            }
        });
    }
}

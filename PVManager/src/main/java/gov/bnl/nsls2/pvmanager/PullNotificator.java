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
public class PullNotificator<T extends PVType<T>> {

    private final PV<T> pv;
    private final PVFunction<T> function;

    public PullNotificator(PV<T> pv, PVFunction<T> aggregator) {
        this.pv = pv;
        this.function = aggregator;
    }

    public void notifyPv() {
        final T newValue = function.getValue();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                pv.getValue().setTo(newValue);
            }
        });
    }
}

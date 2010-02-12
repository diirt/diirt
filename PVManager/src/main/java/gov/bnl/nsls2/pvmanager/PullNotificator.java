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
public class PullNotificator {

    private final PV<TypeDouble> pv;
    private final Aggregator aggregator;

    public PullNotificator(PV<TypeDouble> pv, Aggregator aggregator) {
        this.pv = pv;
        this.aggregator = aggregator;
    }

    public void notifyPv() {
        final double newValue = aggregator.getValue();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                pv.getValue().setDouble(newValue);
            }
        });
    }
}

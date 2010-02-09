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

    private final DoublePV pv;
    private final Aggregator aggregator;

    public PullNotificator(DoublePV pv, Aggregator aggregator) {
        this.pv = pv;
        this.aggregator = aggregator;
    }

    public void notifyPv() {
        final double newValue = aggregator.getValue();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                pv.setValue(newValue);
            }
        });
    }
}

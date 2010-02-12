/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class PVType<T> {

    private List<PVValueChangeListener> valueChangeListeners = new ArrayList<PVValueChangeListener>();

    protected void firePvValueChanged() {
        for (PVValueChangeListener listener : valueChangeListeners) {
            listener.pvValueChanged();
        }
    }

    protected void addPVValueChangeListener(PVValueChangeListener listener) {
        valueChangeListeners.add(listener);
    }

    protected void removePVValueChangeListener(PVValueChangeListener listener) {
        valueChangeListeners.remove(listener);
    }

}

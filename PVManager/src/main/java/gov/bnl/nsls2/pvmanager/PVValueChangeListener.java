/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 * Callback for any change in the PV value. Cannot simply use a PropertyChangedListener
 * because the payload of the PV will be typically updated in place for complex
 * data structures, and therefore the data object is the same and would not
 * trigger a PropertyChangedEvent.
 *
 * @author carcassi
 */
public interface PVValueChangeListener {

    /**
     * Notified when the value of the PV has changed.
     */
    void pvValueChanged();

}

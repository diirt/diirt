/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface DbrCtrl<T extends Number> extends DbrGr<T> {
    T getLowerCtrlLimit();
    T getUpperCtrlLimit();
    void setLowerCtrlLimit(T lowerCtrlLimit);
    void setUpperCtrlLimit(T upperCtrlLimit);
}

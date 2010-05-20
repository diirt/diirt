/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface Ctrl<T extends Number> extends Gr<T> {
    T getLowerCtrlLimit();
    T getUpperCtrlLimit();
    void setLowerCtrlLimit(T lowerCtrlLimit);
    void setUpperCtrlLimit(T upperCtrlLimit);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Limit information needed for control.
 *
 * @author carcassi
 */
public interface Ctrl<T extends Number> extends Gr<T> {
    @Metadata
    T getLowerCtrlLimit();
    @Metadata
    T getUpperCtrlLimit();
    void setLowerCtrlLimit(T lowerCtrlLimit);
    void setUpperCtrlLimit(T upperCtrlLimit);
}

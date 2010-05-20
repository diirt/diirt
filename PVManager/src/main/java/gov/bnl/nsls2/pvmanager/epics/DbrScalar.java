/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface DbrScalar<T> {
    T getValue();
    void setValue(T value);
    ConnectionStatus getConnectionStatus();
    void setConnectionStatus(ConnectionStatus connectionStatus);
}

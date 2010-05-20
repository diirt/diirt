/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Basic type definition for all scalar types.
 * <p>
 * TODO: Does the connection need a status message that can be used in case
 * of errors?
 *
 * @author carcassi
 */
public interface Scalar<T> {
    T getValue();
    void setValue(T value);
    ConnectionStatus getConnectionStatus();
    void setConnectionStatus(ConnectionStatus connectionStatus);
}

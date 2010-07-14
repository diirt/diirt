/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Basic type definition for all scalar types. If the channel was never connected,
 * the value will return null. Otherwise the last value is going to be returned,
 * even if a disconnection occurred.
 *
 * @author carcassi
 */
public interface Scalar<T> {
    T getValue();
    void setValue(T value);
}

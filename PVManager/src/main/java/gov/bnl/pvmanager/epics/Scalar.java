/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Basic type definition for all scalar types. {@link #getValue()} never returns
 * null, even if the channel never connected. One <b>must always look</b>
 * at the alarm severity to be able to correctly interpret the value.
 *
 * @author carcassi
 */
public interface Scalar<T> {
    T getValue();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager;

/**
 *
 * @author carcassi
 */
public abstract class TimedTypeSupport<T> extends TypeSupport<T> {

    public abstract TimeStamp extractTimestamp(T object);

}

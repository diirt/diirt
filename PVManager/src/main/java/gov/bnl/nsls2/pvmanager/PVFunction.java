/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public abstract class PVFunction<T extends PVType> {
    public abstract T getValue();
    public abstract Class<T> getType();
}

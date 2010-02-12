/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 * Aggregates the data out of a Collector into a new data type.
 *
 * @author carcassi
 */
public abstract class Aggregator<T extends PVType> {
    public abstract T getValue();
}

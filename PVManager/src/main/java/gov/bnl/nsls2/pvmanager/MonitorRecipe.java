/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
class MonitorRecipe<T extends PVType<T>> {
    public String pvName;
    public ValueCache<T> cache;
    public Collector collector;
}

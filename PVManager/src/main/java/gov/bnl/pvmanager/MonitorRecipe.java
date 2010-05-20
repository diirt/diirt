/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager;

/**
 * Represents all information needed to create a data connection.
 *
 * @author carcassi
 */
public class MonitorRecipe {
    public String pvName;
    public ValueCache<?> cache;
    public Collector collector;
}

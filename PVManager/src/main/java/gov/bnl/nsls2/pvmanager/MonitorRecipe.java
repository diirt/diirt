/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 * Represents all information needed to create a data connection.
 *
 * @author carcassi
 */
public class MonitorRecipe {
    String pvName;
    ValueCache<?> cache;
    Collector collector;
}

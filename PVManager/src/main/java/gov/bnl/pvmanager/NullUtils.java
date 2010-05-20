/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager;

/**
 *
 * @author carcassi
 */
public class NullUtils {

    /**
     * Returns true if both objects are null or they are equal.
     *
     * @param obj1 first object
     * @param obj2 second object
     * @return true if equals or both are null
     */
    public static boolean equalsOrBothNull(Object obj1, Object obj2) {
        if (obj1 == null)
            return obj2 == null;
        else
            return obj1.equals(obj2);
    }
}

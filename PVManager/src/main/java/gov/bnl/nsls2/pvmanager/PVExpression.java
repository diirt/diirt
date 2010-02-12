/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class PVExpression<T extends PVType<T>> {

    private String name;

    PVExpression(String name) {
        this.name = name;
    }
    String getName() {
        return name;
    }
}

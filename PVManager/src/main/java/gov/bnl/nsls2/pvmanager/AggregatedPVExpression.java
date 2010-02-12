/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class AggregatedPVExpression<T extends PVType<T>> {

    private ConnectionRecipe recipe;

    AggregatedPVExpression(ConnectionRecipe recipe) {
        this.recipe = recipe;
    }

    ConnectionRecipe getRecipe() {
        return recipe;
    }
}

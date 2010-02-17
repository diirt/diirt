/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class PVExpression<T extends PVType<T>> {

    private List<String> pvNames;
    private List<ValueCache<?>> pvCaches;
    private PVFunction<T> function;

    /**
     * Constructor that represents a single pv of a particular type.
     *
     * @param pvName the name of the pv
     * @param pvType the type of the pv
     */
    PVExpression(String pvName, Class<T> pvType) {
        this.pvNames = Collections.singletonList(pvName);
        ValueCache<T> cache = new ValueCache<T>(pvType);
        this.pvCaches = new ArrayList<ValueCache<?>>();
        pvCaches.add(cache);
        this.function = cache;
    }

    private PVExpression(List<PVExpression<?>> childExpressions, PVFunction<T> function) {
        pvNames = new ArrayList<String>();
        pvCaches = new ArrayList<ValueCache<?>>();
        for (PVExpression<?> childExpression : childExpressions) {
            for (int nPv = 0; nPv < childExpression.getPvNames().size(); nPv++) {
                String pvName = childExpression.getPvNames().get(nPv);
                if (pvNames.contains(pvName)) {
                    throw new UnsupportedOperationException("Need to implement functions that take the same PV twice (right now we probably get double notifications)");
                }
                pvNames.add(pvName);
                pvCaches.add(childExpression.getPvData().get(nPv));
            }
        }
        this.function = function;
    }

    List<String> getPvNames() {
        return pvNames;
    }

    List<ValueCache<?>> getPvData() {
        return pvCaches;
    }

    PVFunction<T> getFunction() {
        return function;
    }

    List<MonitorRecipe> createMontiorRecipes(Collector collector) {
        List<MonitorRecipe> recipes = new ArrayList<MonitorRecipe>();
        for (int nPv = 0; nPv < getPvNames().size(); nPv++) {
            MonitorRecipe recipe = new MonitorRecipe();
            recipe.pvName = getPvNames().get(nPv);
            recipe.cache = getPvData().get(nPv);
            recipe.collector = collector;
            recipes.add(recipe);
        }
        return recipes;
    }

}

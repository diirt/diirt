/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.types;

import gov.bnl.nsls2.pvmanager.AggregatedPVExpression;
import gov.bnl.nsls2.pvmanager.Collector;
import gov.bnl.nsls2.pvmanager.MonitorRecipe;
import gov.bnl.nsls2.pvmanager.PVExpression;
import gov.bnl.nsls2.pvmanager.PVFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class CollectionExpressions {

    /**
     * Aggreages the sample at the scan rate and calculates statistical information.
     * @param doublePv the expression to calculate the statistics information on; can't be null
     * @return an expression representing the statistical information of the expression
     */
    public static <T> AggregatedPVExpression<List<T>> listOf(AggregatedPVExpression<T>... expressions) {
        List<MonitorRecipe> recipes = new ArrayList<MonitorRecipe>();
        List<PVFunction> functions = new ArrayList<PVFunction>();
        for (AggregatedPVExpression<T> expression : expressions) {
            recipes.addAll(expression.getMonitorRecipes());
            functions.add(expression.getFunction());
        }
        String name = null;
        
        if (expressions.length < 10) {
            name = "list" + Arrays.toString(expressions);
        } else {
            name = "list(...)";
        }

        return new AggregatedPVExpression<List<T>>(recipes,
                (Class<List<T>>) (Class) List.class, (PVFunction<List<T>>) (PVFunction) new ListOfFunction(functions), name);
    }

}

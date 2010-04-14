/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.types;

import gov.bnl.nsls2.pvmanager.AggregatedExpression;
import gov.bnl.nsls2.pvmanager.MonitorRecipe;
import gov.bnl.nsls2.pvmanager.Function;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguage {

    /**
     * Converts a list of expressions to and expression that returns the list of results.
     * @param expression a list of expressions
     * @return an expression representing the list of results
     */
    public static <T> AggregatedExpression<List<T>> listOf(AggregatedExpression<T>... expressions) {
        // The connections needed are the union of all connections.
        // Calculate all the needed functions to combine
        List<MonitorRecipe> recipes = new ArrayList<MonitorRecipe>();
        List<Function> functions = new ArrayList<Function>();
        for (AggregatedExpression<T> expression : expressions) {
            recipes.addAll(expression.getMonitorRecipes());
            functions.add(expression.getFunction());
        }

        // If the list of expression is large, the name is going to be big
        // and it might trigger an OutOfMemoryException just for this.
        // We cap the list of names to 10
        String name = null;
        if (expressions.length < 10) {
            name = "list" + Arrays.toString(expressions);
        } else {
            name = "list(...)";
        }

        @SuppressWarnings("unchecked")
        AggregatedExpression<List<T>> expression = new AggregatedExpression<List<T>>(recipes,
                (Class<List<T>>) (Class) List.class, (Function<List<T>>) (Function) new ListOfFunction(functions), name);
        return expression;
    }

}

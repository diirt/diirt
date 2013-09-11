/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import java.util.Collection;
import static org.epics.pvmanager.formula.BaseTestForFormula.compare;
import org.epics.vtype.VNumber;
import static org.epics.vtype.ValueFactory.newVDouble;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 *
 * @author carcassi
 */
public class FunctionTester {
    
    private final FormulaFunction function;
    
    private FunctionTester(FormulaFunction function) {
        this.function = function;
    }
    
    public static FunctionTester findByName(FormulaFunctionSet set, String name) {
        Collection<FormulaFunction> functions = set.findFunctions(name);
	assertThat("Function '" + name + "' not found.", functions.isEmpty(),
		equalTo(false));
	assertThat("Multiple matches for function '" + name + "'.", functions.size(),
		equalTo(1));
        return new FunctionTester(functions.iterator().next());
    }

    public FunctionTester compareReturnValue(Object expected, Object... args) {
	Object result = function.calculate(Arrays.asList(args));
	assertThat(
		"Wrong result for function '" + function.getName() + "("
			+ Arrays.toString(args) + ")'. Was (" + result
			+ ") expected (" + expected + ")",
		compare(result, expected), equalTo(true));
        return this;
    }

    public FunctionTester compareReturnValue(double result, double arg1, double arg2) {
	VNumber value = (VNumber) function.calculate(Arrays.<Object> asList(
		newVDouble(arg1), newVDouble(arg2)));
	assertThat("Wrong result for function '" + function.getName() + "(" + arg1 + ", "
		+ arg2 + ")'.", value.getValue().doubleValue(),
		closeTo(result, 0.0001));
        return this;
    }
    
    
}

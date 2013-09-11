/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import java.util.Collection;
import static org.epics.pvmanager.formula.BaseTestForFormula.compare;
import static org.epics.pvmanager.formula.BaseTestForFormula.compareAlarm;
import static org.epics.pvmanager.formula.BaseTestForFormula.testFunctionAlarm;
import org.epics.util.text.NumberFormats;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.VNumber;
import org.epics.vtype.VTypeToString;
import static org.epics.vtype.ValueFactory.*;
import org.epics.vtype.ValueUtil;
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

    public FunctionTester compareReturnAlarm(Alarm expected, Object... args) {
	Alarm result = ValueUtil.alarmOf(function.calculate(Arrays.asList(args)));
	assertThat(
		"Wrong result for function '" + function.getName() + "("
			+ Arrays.toString(args) + ")'. Was (" + VTypeToString.alarmToString(result)
			+ ") expected (" + VTypeToString.alarmToString(expected) + ")",
		compareAlarm(result, expected), equalTo(true));
        return this;
    }
    
    public FunctionTester highestAlarmReturned() {
        if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class, VNumber.class))) {
            twoArgNumericHighestAlarmReturned();
        } else {
            throw new IllegalArgumentException("Can't test highest alarm returned for " + function.getName());
        }
        return this;
    }
    
    private void twoArgNumericHighestAlarmReturned() {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        compareReturnAlarm(alarmNone(), newVDouble(0.0, display), newVDouble(1.0, display));
        compareReturnAlarm(newAlarm(AlarmSeverity.MINOR, "HIGH"), newVDouble(1.0, display), newVDouble(3.5, display));
        compareReturnAlarm(newAlarm(AlarmSeverity.MAJOR, "LOLO"), newVDouble(-5.0, display), newVDouble(3.5, display));
    }
    
}

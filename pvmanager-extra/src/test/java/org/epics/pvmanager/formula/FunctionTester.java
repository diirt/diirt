/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import java.util.Collection;
import static org.epics.pvmanager.formula.BaseTestForFormula.compare;
import static org.epics.pvmanager.formula.BaseTestForFormula.compareAlarm;
import static org.epics.pvmanager.formula.BaseTestForFormula.compareTime;
import org.epics.util.text.NumberFormats;
import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VNumber;
import org.epics.vtype.VTypeToString;
import org.epics.vtype.VTypeValueEquals;
import static org.epics.vtype.ValueFactory.*;
import org.epics.vtype.ValueUtil;
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
    
    public static FunctionTester findBySignature(FormulaFunctionSet set, String name, Class<?>... argTypes) {
        Collection<FormulaFunction> functions = set.findFunctions(name);
	assertThat("Function '" + name + "' not found.", functions.isEmpty(),
		equalTo(false));
        
        functions = FormulaFunctions.findArgTypeMatch(Arrays.asList(argTypes), functions);
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

    public FunctionTester compareReturnValue(double result, double arg1) {
	VNumber value = (VNumber) function.calculate(Arrays.<Object> asList(
		newVDouble(arg1)));
	assertThat("Wrong result for function '" + function.getName() + "(" + arg1 + 
                ")'.", value.getValue().doubleValue(),
		closeTo(result, 0.0001));
        return this;
    }

    public FunctionTester compareReturnAlarm(Alarm expected, Object... args) {
	Alarm result = ValueUtil.alarmOf(function.calculate(Arrays.asList(args)));
	assertThat(
		"Wrong result for function '" + function.getName() + "("
			+ Arrays.toString(args) + ")'. Was (" + VTypeToString.alarmToString(result)
			+ ") expected (" + VTypeToString.alarmToString(expected) + ")",
                VTypeValueEquals.alarmEquals(result, expected), equalTo(true));
        return this;
    }

    public FunctionTester compareReturnTime(Time expected, Object... args) {
	Time result = ValueUtil.timeOf(function.calculate(Arrays.asList(args)));
	assertThat(
		"Wrong result for function '" + function.getName() + "("
			+ Arrays.toString(args) + ")'. Was (" + VTypeToString.timeToString(result)
			+ ") expected (" + VTypeToString.timeToString(expected) + ")",
		VTypeValueEquals.timeEquals(result, expected), equalTo(true));
        return this;
    }
    
    public FunctionTester highestAlarmReturned() {
        if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class))) {
            oneArgNumericHighestAlarmReturned();
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class, VNumber.class))) {
            twoArgNumericHighestAlarmReturned();
        } else {
            throw new IllegalArgumentException("Can't test highest alarm returned for " + function.getName());
        }
        return this;
    }
    
    public void oneArgNumericHighestAlarmReturned() {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        compareReturnAlarm(alarmNone(), newVDouble(0.0, display));
        compareReturnAlarm(newAlarm(AlarmSeverity.MINOR, "HIGH"), newVDouble(3.5, display));
        compareReturnAlarm(newAlarm(AlarmSeverity.MAJOR, "LOLO"), newVDouble(-5.0, display));
    }
    
    private void twoArgNumericHighestAlarmReturned() {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        compareReturnAlarm(alarmNone(), newVDouble(0.0, display), newVDouble(1.0, display));
        compareReturnAlarm(newAlarm(AlarmSeverity.MINOR, "HIGH"), newVDouble(1.0, display), newVDouble(3.5, display));
        compareReturnAlarm(newAlarm(AlarmSeverity.MAJOR, "LOLO"), newVDouble(-5.0, display), newVDouble(3.5, display));
    }
    
    public FunctionTester latestTimeReturned() {
        if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class))) {
            oneArgNumericLatestTimeReturned();
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class, VNumber.class))) {
            twoArgNumericLatestTimeReturned();
        } else {
            throw new IllegalArgumentException("Can't test latest time returned for " + function.getName());
        }
        return this;
    }
    
    private void oneArgNumericLatestTimeReturned() {
        Time time1 = newTime(Timestamp.of(12340000, 0));
        Time time2 = newTime(Timestamp.of(12350000, 0));
        compareReturnTime(time1, newVDouble(0.0, time1));
        compareReturnTime(time2, newVDouble(0.0, time2));
    }
    
    private void twoArgNumericLatestTimeReturned() {
        Time time1 = newTime(Timestamp.of(12340000, 0));
        Time time2 = newTime(Timestamp.of(12350000, 0));
        compareReturnTime(time1, newVDouble(0.0, time1), newVDouble(1.0, time1));
        compareReturnTime(time2, newVDouble(0.0, time1), newVDouble(1.0, time2));
        compareReturnTime(time2, newVDouble(0.0, time2), newVDouble(1.0, time1));
    }
}

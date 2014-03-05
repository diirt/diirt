/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import java.util.Collection;
import static org.epics.pvmanager.formula.BaseTestForFormula.compare;
import static org.epics.pvmanager.formula.BaseTestForFormula.compareAlarm;
import static org.epics.pvmanager.formula.BaseTestForFormula.compareTime;
import org.epics.util.array.ArrayDouble;
import org.epics.util.text.NumberFormats;
import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
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
	assertThat("No matches found for function '" + name + "'.", functions.isEmpty(),
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
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumberArray.class, VNumberArray.class))) {
            twoArgNumericArrayHighestAlarmReturned();
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumberArray.class, VNumber.class))) {
            highestAlarmReturnedVNumberArrayVNumber();
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class, VNumberArray.class))) {
            highestAlarmReturnedVNumberVNumberArray();
        } else {
            highestAlarmReturned(function);
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
    
    private void twoArgNumericArrayHighestAlarmReturned() {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        Alarm none = alarmNone();
        Alarm minor = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Alarm major = newAlarm(AlarmSeverity.MAJOR, "LOLO");
        compareReturnAlarm(none, newVNumberArray(new ArrayDouble(1.0), none, timeNow(), display), newVNumberArray(new ArrayDouble(1.0), none, timeNow(), display));
        compareReturnAlarm(minor, newVNumberArray(new ArrayDouble(1.0), none, timeNow(), display), newVNumberArray(new ArrayDouble(1.0), minor, timeNow(), display));
        compareReturnAlarm(major, newVNumberArray(new ArrayDouble(1.0), major, timeNow(), display), newVNumberArray(new ArrayDouble(1.0), minor, timeNow(), display));
    }
    
    private void highestAlarmReturnedVNumberArrayVNumber() {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        Alarm none = alarmNone();
        Alarm minor = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Alarm major = newAlarm(AlarmSeverity.MAJOR, "LOLO");
        compareReturnAlarm(none, newVNumberArray(new ArrayDouble(1.0), none, timeNow(), display), newVNumber(1.0, none, timeNow(), display));
        compareReturnAlarm(minor, newVNumberArray(new ArrayDouble(1.0), none, timeNow(), display), newVNumber(1.0, minor, timeNow(), display));
        compareReturnAlarm(major, newVNumberArray(new ArrayDouble(1.0), major, timeNow(), display), newVNumber(1.0, minor, timeNow(), display));
    }
    
    private void highestAlarmReturnedVNumberVNumberArray() {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        Alarm none = alarmNone();
        Alarm minor = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Alarm major = newAlarm(AlarmSeverity.MAJOR, "LOLO");
        compareReturnAlarm(none, newVNumber(1.0, none, timeNow(), display), newVNumberArray(new ArrayDouble(1.0), none, timeNow(), display));
        compareReturnAlarm(minor, newVNumber(1.0, none, timeNow(), display), newVNumberArray(new ArrayDouble(1.0), minor, timeNow(), display));
        compareReturnAlarm(major, newVNumber(1.0, major, timeNow(), display), newVNumberArray(new ArrayDouble(1.0), minor, timeNow(), display));
    }

    private Object createValue(Class<?> clazz, Alarm alarm, Time time, Display display) {
        if (clazz.equals(VNumber.class)) {
            return newVNumber(1.0, alarm, time, display);
        } else if (clazz.equals(VNumberArray.class)) {
            return newVNumberArray(new ArrayDouble(1.0), alarm, time, display);
        } else if (clazz.equals(VString.class)) {
            return newVString("A", alarm, time);
        } else {
            throw new IllegalArgumentException("Can't create sample argument for class " + clazz);
        }
    }
    
    private void highestAlarmReturned(FormulaFunction function) {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        Object[] args;
        if (function.isVarArgs()) {
            args = new Object[function.getArgumentTypes().size() + 1];
        } else {
            args = new Object[function.getArgumentTypes().size()];
        }
        Alarm none = alarmNone();
        Alarm minor = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Alarm major = newAlarm(AlarmSeverity.MAJOR, "LOLO");
        
        // Prepare arguments with no alarm
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            args[i] = createValue(function.getArgumentTypes().get(i), none, timeNow(), display);
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), none, timeNow(), display);
        }
        compareReturnAlarm(none, args);
        
        // Prepare arguments with one minor and everything else none
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            if (i == args.length - 1) {
                args[i] = createValue(function.getArgumentTypes().get(i), none, timeNow(), display);
            } else {
                args[i] = createValue(function.getArgumentTypes().get(i), minor, timeNow(), display);
            }
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), none, timeNow(), display);
        }
        compareReturnAlarm(minor, args);
        
        // Prepare arguments with one minor and everything else major
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            if (i == args.length - 1) {
                args[i] = createValue(function.getArgumentTypes().get(i), major, timeNow(), display);
            } else {
                args[i] = createValue(function.getArgumentTypes().get(i), minor, timeNow(), display);
            }
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), major, timeNow(), display);
        }
        compareReturnAlarm(major, args);
    }
    
    public FunctionTester latestTimeReturned() {
        if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class))) {
            oneArgNumericLatestTimeReturned();
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class, VNumber.class))) {
            twoArgNumericLatestTimeReturned();
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumberArray.class, VNumberArray.class))) {
            twoArgNumericArrayLatestTimeReturned();
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumberArray.class, VNumber.class))) {
            latestTimeReturnedVNumberArrayVNumber();
        } else if (function.getArgumentTypes().equals(Arrays.asList(VNumber.class, VNumberArray.class))) {
            latestTimeReturnedVNumberVNumberArray();
        } else {
            latestTimeReturned(function);
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
    
    private void twoArgNumericArrayLatestTimeReturned() {
        Time time1 = newTime(Timestamp.of(12340000, 0));
        Time time2 = newTime(Timestamp.of(12350000, 0));
        compareReturnTime(time1, newVNumberArray(new ArrayDouble(1.0), alarmNone(), time1, displayNone()), newVNumberArray(new ArrayDouble(2.0), alarmNone(), time1, displayNone()));
        compareReturnTime(time2, newVNumberArray(new ArrayDouble(1.0), alarmNone(), time1, displayNone()), newVNumberArray(new ArrayDouble(2.0), alarmNone(), time2, displayNone()));
        compareReturnTime(time2, newVNumberArray(new ArrayDouble(1.0), alarmNone(), time2, displayNone()), newVNumberArray(new ArrayDouble(2.0), alarmNone(), time1, displayNone()));
    }
    
    private void latestTimeReturnedVNumberArrayVNumber() {
        Time time1 = newTime(Timestamp.of(12340000, 0));
        Time time2 = newTime(Timestamp.of(12350000, 0));
        compareReturnTime(time1, newVNumberArray(new ArrayDouble(1.0), alarmNone(), time1, displayNone()), newVNumber(2.0, alarmNone(), time1, displayNone()));
        compareReturnTime(time2, newVNumberArray(new ArrayDouble(1.0), alarmNone(), time1, displayNone()), newVNumber(2.0, alarmNone(), time2, displayNone()));
        compareReturnTime(time2, newVNumberArray(new ArrayDouble(1.0), alarmNone(), time2, displayNone()), newVNumber(2.0, alarmNone(), time1, displayNone()));
    }
    
    private void latestTimeReturnedVNumberVNumberArray() {
        Time time1 = newTime(Timestamp.of(12340000, 0));
        Time time2 = newTime(Timestamp.of(12350000, 0));
        compareReturnTime(time1, newVNumber(2.0, alarmNone(), time1, displayNone()), newVNumberArray(new ArrayDouble(1.0), alarmNone(), time1, displayNone()));
        compareReturnTime(time2, newVNumber(2.0, alarmNone(), time1, displayNone()), newVNumberArray(new ArrayDouble(1.0), alarmNone(), time2, displayNone()));
        compareReturnTime(time2, newVNumber(2.0, alarmNone(), time2, displayNone()), newVNumberArray(new ArrayDouble(1.0), alarmNone(), time1, displayNone()));
    }
    
    private void latestTimeReturned(FormulaFunction function) {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        Object[] args;
        if (function.isVarArgs()) {
            args = new Object[function.getArgumentTypes().size() + 1];
        } else {
            args = new Object[function.getArgumentTypes().size()];
        }
        Time time1 = newTime(Timestamp.of(12340000, 0));
        Time time2 = newTime(Timestamp.of(12350000, 0));
        
        // Prepare arguments with all time1
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            args[i] = createValue(function.getArgumentTypes().get(i), alarmNone(), time1, display);
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), alarmNone(), time1, display);
        }
        compareReturnTime(time1, args);
        
        // Prepare arguments with one time2 and everything else time1
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            if (i == args.length - 1) {
                args[i] = createValue(function.getArgumentTypes().get(i), alarmNone(), time1, display);
            } else {
                args[i] = createValue(function.getArgumentTypes().get(i), alarmNone(), time2, display);
            }
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), alarmNone(), time1, display);
        }
        compareReturnTime(time2, args);
        
        // Prepare arguments with one minor and everything else major
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            if (i == args.length - 1) {
                args[i] = createValue(function.getArgumentTypes().get(i), alarmNone(), time2, display);
            } else {
                args[i] = createValue(function.getArgumentTypes().get(i), alarmNone(), time1, display);
            }
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), alarmNone(), time2, display);
        }
        compareReturnTime(time2, args);
    }
}

/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.diirt.util.array.ArrayDouble;
import org.diirt.util.text.NumberFormats;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.Display;
import org.diirt.vtype.Time;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VType;
import org.diirt.vtype.VTypeToString;
import org.diirt.vtype.VTypeValueEquals;
import org.diirt.vtype.ValueFactory;

import static org.diirt.vtype.ValueFactory.*;

import org.diirt.vtype.ValueUtil;
import org.diirt.vtype.table.Column;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

import org.junit.Assert;

import static org.junit.Assert.assertThat;

/**
 *
 * @author carcassi
 */
public class FunctionTester {

    private final FormulaFunction function;
    private boolean convertTypes = true;

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

    public FunctionTester convertTypes(boolean convertTypes) {
        this.convertTypes = convertTypes;
        return this;
    }

    public FunctionTester matchReturnValue(Matcher<Object> matcher, Object... args) {
        if (convertTypes) {
            args = convertTypes(args);
        }
        Object result = function.calculate(Arrays.asList(args));
        Assert.assertThat(result, matcher);
        return this;
    }

    public FunctionTester compareReturnValue(Object expected, Object... args) {
        if (convertTypes) {
            expected = convertType(expected);
            args = convertTypes(args);
        }
        Object result = function.calculate(Arrays.asList(args));
        if (result instanceof VDouble && expected instanceof VDouble) {
            assertThat("Wrong result for function '" + function.getName() + "("
                    + Arrays.toString(args) + ")'.", ((VDouble) result).getValue().doubleValue(),
                closeTo(((VDouble) expected).getValue().doubleValue(), 0.0001));
        } else {
            assertThat(
                    "Wrong result for function '" + function.getName() + "("
                            + Arrays.toString(args) + ")'. Was (" + result
                            + ") expected (" + expected + ")",
                    compareValues(result, expected), equalTo(true));
        }
        return this;
    }

    public static boolean compareValues(Object obj1, Object obj2) {
        if (Objects.equals(obj1, obj2)) {
            return true;
        }
        if (obj1 instanceof VType && obj2 instanceof VType) {
            return VTypeValueEquals.valueEquals(obj1, obj2);
        } else if (obj1 instanceof Column && obj2 instanceof Column) {
            Column column1 = (Column) obj1;
            Column column2 = (Column) obj2;
            return column1.getName().equals(column2.getName()) &&
                    column1.isGenerated() == column2.isGenerated() &&
                    column1.getType().equals(column2.getType()) &&
                    column1.getData(column1.isGenerated() ? 10 : -1).equals(column2.getData(column2.isGenerated() ? 10 : -1));
        }
        return false;
    }

    private Object convertType(Object obj) {
        if (obj instanceof VType) {
            return obj;
        }
        Object converted = ValueFactory.toVType(obj);
        if (converted != null) {
            return converted;
        }
        return obj;
    }

    private Object[] convertTypes(Object... obj) {
        Object[] result = new Object[obj.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = convertType(obj[i]);

        }
        return result;
    }

    public FunctionTester compareReturnAlarm(Alarm expected, Object... args) {
        if (convertTypes) {
            args = convertTypes(args);
        }
        Alarm result = ValueUtil.alarmOf(function.calculate(Arrays.asList(args)));
        assertThat(
                "Wrong result for function '" + function.getName() + "("
                        + Arrays.toString(args) + ")'. Was (" + VTypeToString.alarmToString(result)
                        + ") expected (" + VTypeToString.alarmToString(expected) + ")",
                VTypeValueEquals.alarmEquals(result, expected), equalTo(true));
        return this;
    }

    public FunctionTester compareReturnTime(Time expected, Object... args) {
        if (convertTypes) {
            args = convertTypes(args);
        }
        Time result = ValueUtil.timeOf(function.calculate(Arrays.asList(args)));
        assertThat(
                "Wrong result for function '" + function.getName() + "("
                        + Arrays.toString(args) + ")'. Was (" + VTypeToString.timeToString(result)
                        + ") expected (" + VTypeToString.timeToString(expected) + ")",
                VTypeValueEquals.timeEquals(result, expected), equalTo(true));
        return this;
    }

    public FunctionTester highestAlarmReturned() {
        if (function.isVarArgs() || function.getArgumentTypes().size() > 1) {
            highestAlarmReturnedMultipleArgs(function);
        } else {
            highestAlarmReturnedSingleArg(function);
        }
        return this;
    }

    private Object createValue(Class<?> clazz, Alarm alarm, Time time, Display display) {
        if (clazz.equals(VNumber.class)) {
            return newVNumber(1.0, alarm, time, display);
        } else if (clazz.equals(VNumberArray.class)) {
            return newVNumberArray(new ArrayDouble(1.0), alarm, time, display);
        } else if (clazz.equals(VString.class)) {
            return newVString("A", alarm, time);
        } else if (clazz.equals(VStringArray.class)) {
            return newVStringArray(Arrays.asList("A"), alarm, time);
        } else if (clazz.equals(VBoolean.class)) {
            return newVBoolean(true, alarm, time);
        } else {
            throw new IllegalArgumentException("Can't create sample argument for class " + clazz);
        }
    }

    private void highestAlarmReturnedSingleArg(FormulaFunction function) {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        Alarm none = alarmNone();
        Alarm minor = newAlarm(AlarmSeverity.MINOR, "HIGH");
        Alarm major = newAlarm(AlarmSeverity.MAJOR, "LOLO");

        compareReturnAlarm(none, createValue(function.getArgumentTypes().get(0), none, timeNow(), display));
        compareReturnAlarm(minor, createValue(function.getArgumentTypes().get(0), minor, timeNow(), display));
        compareReturnAlarm(major, createValue(function.getArgumentTypes().get(0), major, timeNow(), display));
    }

    private void highestAlarmReturnedMultipleArgs(FormulaFunction function) {
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
        if (function.isVarArgs() || function.getArgumentTypes().size() > 1) {
            latestTimeReturnedMultipleArgs(function);
        } else {
            latestTimeReturnedSingleArg(function);
        }
        return this;
    }

    private void latestTimeReturnedSingleArg(FormulaFunction function) {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        Object[] args;
        Time time1 = newTime(Instant.ofEpochSecond(12340000, 0));
        Time time2 = newTime(Instant.ofEpochSecond(12350000, 0));

        compareReturnTime(time1, createValue(function.getArgumentTypes().get(0), alarmNone(), time1, display));
        compareReturnTime(time2, createValue(function.getArgumentTypes().get(0), alarmNone(), time2, display));
    }

    private void latestTimeReturnedMultipleArgs(FormulaFunction function) {
        Display display = newDisplay(-5.0, -4.0, -3.0, "m", NumberFormats.toStringFormat(), 3.0, 4.0, 5.0, -5.0, 5.0);
        Object[] args;
        if (function.isVarArgs()) {
            args = new Object[function.getArgumentTypes().size() + 1];
        } else {
            args = new Object[function.getArgumentTypes().size()];
        }
        Time time1 = newTime(Instant.ofEpochSecond(12340000, 0));
        Time time2 = newTime(Instant.ofEpochSecond(12350000, 0));

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

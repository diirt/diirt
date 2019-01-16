/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.diirt.vtype.table.Column;
import org.epics.util.array.ArrayDouble;
import org.epics.util.stats.Range;
import org.epics.util.text.NumberFormats;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VDouble;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VType;
import org.hamcrest.Matcher;
import org.junit.Assert;

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
            return obj1.equals(obj2);
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
        Object converted = VType.toVType(obj);
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
        Alarm result = Alarm.alarmOf(function.calculate(Arrays.asList(args)));
        assertThat(
                "Wrong result for function '" + function.getName() + "("
                        + Arrays.toString(args) + ")'. Was (" + Alarm.alarmOf(result)
                        + ") expected (" + Alarm.alarmOf(expected) + ")",
                result.equals(expected), equalTo(true));
        return this;
    }

    public FunctionTester compareReturnTime(Time expected, Object... args) {
        if (convertTypes) {
            args = convertTypes(args);
        }
        Time result = Time.timeOf(function.calculate(Arrays.asList(args)));
        assertThat(
                "Wrong result for function '" + function.getName() + "("
                        + Arrays.toString(args) + ")'. Was (" + Time.timeOf(result)
                        + ") expected (" + Time.timeOf(expected) + ")",
                result.equals(expected), equalTo(true));
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
            return VNumber.of(1.0, alarm, time, display);
        } else if (clazz.equals(VNumberArray.class)) {
            return VNumberArray.of(ArrayDouble.of(1.0), alarm, time, display);
        } else if (clazz.equals(VString.class)) {
            return VString.of("A", alarm, time);
        } else if (clazz.equals(VStringArray.class)) {
            return VStringArray.of(Arrays.asList("A"), alarm, time);
        } else if (clazz.equals(VBoolean.class)) {
            return VBoolean.of(true, alarm, time);
        } else {
            throw new IllegalArgumentException("Can't create sample argument for class " + clazz);
        }
    }

    private void highestAlarmReturnedSingleArg(FormulaFunction function) {
        Display display = Display.of(Range.of(-5.0, 5.0), Range.of(-4.0, 4.0), Range.of(-3.0, 3.0), Range.of(-5.0, 5.0),
                "m", NumberFormats.toStringFormat());
        Alarm none = Alarm.none();
        Alarm minor = Alarm.of(AlarmSeverity.MINOR, null, "HIGH");
        Alarm major = Alarm.of(AlarmSeverity.MAJOR, null, "LOLO");

        compareReturnAlarm(none, createValue(function.getArgumentTypes().get(0), none, Time.now(), display));
        compareReturnAlarm(minor, createValue(function.getArgumentTypes().get(0), minor, Time.now(), display));
        compareReturnAlarm(major, createValue(function.getArgumentTypes().get(0), major, Time.now(), display));
    }

    private void highestAlarmReturnedMultipleArgs(FormulaFunction function) {
        Display display = Display.of(Range.of(-5.0, 5.0), Range.of(-4.0, 4.0), Range.of(-3.0, 3.0), Range.of(-5.0, 5.0),
                "m", NumberFormats.toStringFormat());
        Object[] args;
        if (function.isVarArgs()) {
            args = new Object[function.getArgumentTypes().size() + 1];
        } else {
            args = new Object[function.getArgumentTypes().size()];
        }
        Alarm none = Alarm.none();
        Alarm minor = Alarm.of(AlarmSeverity.MINOR, null, "HIGH");
        Alarm major = Alarm.of(AlarmSeverity.MAJOR, null, "LOLO");

        // Prepare arguments with no alarm
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            args[i] = createValue(function.getArgumentTypes().get(i), none, Time.now(), display);
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), none, Time.now(), display);
        }
        compareReturnAlarm(none, args);

        // Prepare arguments with one minor and everything else none
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            if (i == args.length - 1) {
                args[i] = createValue(function.getArgumentTypes().get(i), none, Time.now(), display);
            } else {
                args[i] = createValue(function.getArgumentTypes().get(i), minor, Time.now(), display);
            }
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), none, Time.now(), display);
        }
        compareReturnAlarm(minor, args);

        // Prepare arguments with one minor and everything else major
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            if (i == args.length - 1) {
                args[i] = createValue(function.getArgumentTypes().get(i), major, Time.now(), display);
            } else {
                args[i] = createValue(function.getArgumentTypes().get(i), minor, Time.now(), display);
            }
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), major, Time.now(), display);
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
        Display display = Display.of(Range.of(-5.0, 5.0), Range.of(-4.0, 4.0), Range.of(-3.0, 3.0), Range.of(-5.0, 5.0),
                "m", NumberFormats.toStringFormat());
        Object[] args;
        Time time1 = Time.of(Instant.ofEpochSecond(12340000, 0));
        Time time2 = Time.of(Instant.ofEpochSecond(12350000, 0));

        compareReturnTime(time1, createValue(function.getArgumentTypes().get(0), Alarm.none(), time1, display));
        compareReturnTime(time2, createValue(function.getArgumentTypes().get(0), Alarm.none(), time2, display));
    }

    private void latestTimeReturnedMultipleArgs(FormulaFunction function) {
        Display display = Display.of(Range.of(-5.0, 5.0), Range.of(-4.0, 4.0), Range.of(-3.0, 3.0), Range.of(-5.0, 5.0),
                "m", NumberFormats.toStringFormat());
        Object[] args;
        if (function.isVarArgs()) {
            args = new Object[function.getArgumentTypes().size() + 1];
        } else {
            args = new Object[function.getArgumentTypes().size()];
        }
        Time time1 = Time.of(Instant.ofEpochSecond(12340000, 0));
        Time time2 = Time.of(Instant.ofEpochSecond(12350000, 0));

        // Prepare arguments with all time1
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            args[i] = createValue(function.getArgumentTypes().get(i), Alarm.none(), time1, display);
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), Alarm.none(), time1, display);
        }
        compareReturnTime(time1, args);

        // Prepare arguments with one time2 and everything else time1
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            if (i == args.length - 1) {
                args[i] = createValue(function.getArgumentTypes().get(i), Alarm.none(), time1, display);
            } else {
                args[i] = createValue(function.getArgumentTypes().get(i), Alarm.none(), time2, display);
            }
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), Alarm.none(), time1, display);
        }
        compareReturnTime(time2, args);

        // Prepare arguments with one minor and everything else major
        for (int i = 0; i < function.getArgumentTypes().size(); i++) {
            if (i == args.length - 1) {
                args[i] = createValue(function.getArgumentTypes().get(i), Alarm.none(), time2, display);
            } else {
                args[i] = createValue(function.getArgumentTypes().get(i), Alarm.none(), time1, display);
            }
        }
        if (function.isVarArgs()) {
            args[args.length - 1] = createValue(function.getArgumentTypes().get(args.length - 2), Alarm.none(), time2, display);
        }
        compareReturnTime(time2, args);
    }
}

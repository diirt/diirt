/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.vstring;

import java.util.Arrays;
import java.util.List;
import org.diirt.datasource.formula.FormulaFunction;
import org.epics.vtype.Alarm;
import org.epics.vtype.Time;
import org.epics.vtype.VString;
import org.epics.vtype.VType;

import org.diirt.vtype.util.ValueUtil;

/**
 * @author shroffk
 *
 */
class ToStringFunction implements FormulaFunction {

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return true;
    }

    @Override
    public String getName() {
        return "toString";
    }

    @Override
    public String getDescription() {
        return "Convert the value to a string";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>> asList(VType.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("value");
    }

    @Override
    public Class<?> getReturnType() {
        return VType.class;
    }

    @Override
    public Object calculate(List<Object> args) {
        VType value = (VType) args.get(0);
        Alarm alarm = Alarm.alarmOf(value);
        if (alarm == null) {
            alarm = Alarm.none();
        }
        Time time = Time.timeOf(value);
        if (time == null) {
            time = Time.now();
        }

        return VString.of(ValueUtil.getDefaultValueFormat().format(value),
                alarm,
                time);

    }

}

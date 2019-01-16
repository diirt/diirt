/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.alarm;

import java.util.Arrays;
import java.util.List;
import org.diirt.datasource.formula.FormulaFunction;
import org.diirt.vtype.util.ValueUtil;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.EnumDisplay;
import org.epics.vtype.Time;
import org.epics.vtype.VEnum;
import org.epics.vtype.VType;

/**
 * Retrieves the highest alarm from the values.
 *
 * @author carcassi
 */
class HighestSeverityFunction implements FormulaFunction {

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
        return "highestSeverity";
    }

    @Override
    public String getDescription() {
        return "Returns the highest severity";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>>asList(VType.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("values");
    }

    @Override
    public Class<?> getReturnType() {
        return VEnum.class;
    }

    @Override
    public Object calculate(final List<Object> args) {
        Alarm alarm = ValueUtil.highestSeverityOf(args, true);
        Time time = Time.timeOf(alarm);
        if (time == null) {
            time = Time.now();
        }

        return VEnum.of(alarm.getSeverity().ordinal(), EnumDisplay.of(AlarmSeverity.labels()), alarm, time);
    }

}

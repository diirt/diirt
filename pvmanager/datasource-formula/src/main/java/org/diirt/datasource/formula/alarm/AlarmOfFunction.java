/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.alarm;

import java.util.Arrays;
import java.util.List;

import org.diirt.datasource.formula.FormulaFunction;
import org.diirt.datasource.util.NullUtils;
import org.diirt.vtype.util.ValueUtil;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.EnumDisplay;
import org.epics.vtype.VEnum;

/**
 * Extract the alarm of a value as a VEnum.
 *
 * @author carcassi
 */
class AlarmOfFunction implements FormulaFunction {

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public String getName() {
        return "alarmOf";
    }

    @Override
    public String getDescription() {
        return "The alarm severity as a VEnum";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>>asList(Object.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("arg");
    }

    @Override
    public Class<?> getReturnType() {
        return VEnum.class;
    }

    @Override
    public Object calculate(List<Object> args) {
        if (NullUtils.containsNull(args)) {
            return null;
        }
        Object arg = args.get(0);
        Alarm alarm = Alarm.alarmOf(arg);
        return VEnum.of(alarm.getSeverity().ordinal(), EnumDisplay.of(AlarmSeverity.labels()),
                alarm,
                ValueUtil.latestValidTimeOrNowOf(args));
    }

}

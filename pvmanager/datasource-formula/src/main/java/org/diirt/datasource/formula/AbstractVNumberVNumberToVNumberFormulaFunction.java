/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.util.Arrays;
import java.util.List;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.Time;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueFactory;
import org.diirt.vtype.ValueUtil;


/**
 * Abstract class for formula functions that take two VNumbers as arguments
 * and return a VNumber.
 * <p>
 * This class takes care of:
 * <ul>
 *    <li>extracting the Number from the VNumbers</li>
 *    <li>null handling - returns null if one argument is null</li>
 *    <li>alarm handling - returns highest alarm</li>
 *    <li>time handling - returns latest time, or now if no time is available</li>
 *    <li>display handling - returns display none</li>
 * </ul>
 *
 * @author carcassi
 */
public abstract class AbstractVNumberVNumberToVNumberFormulaFunction implements FormulaFunction {

    private final String name;
    private final String description;
    private final List<Class<?>> argumentTypes;
    private final List<String> argumentNames;
    
    public AbstractVNumberVNumberToVNumberFormulaFunction(String name, String description, String arg1Name, String arg2Name) {
        this.name = name;
        this.description = description;
        this.argumentTypes = Arrays.<Class<?>>asList(VNumber.class, VNumber.class);
        this.argumentNames = Arrays.asList(arg1Name, arg2Name);
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public final boolean isPure() {
        return true;
    }

    @Override
    public final boolean isVarArgs() {
        return false;
    }

    @Override
    public final List<Class<?>> getArgumentTypes() {
        return argumentTypes;
    }

    @Override
    public final List<String> getArgumentNames() {
        return argumentNames;
    }

    @Override
    public final Class<?> getReturnType() {
        return VNumber.class;
    }

    @Override
    public final Object calculate(List<Object> args) {
        Object arg1 = args.get(0);
        Object arg2 = args.get(1);
        if (arg1 == null || arg2 == null) {
            return null;
        }
        Alarm alarm = ValueUtil.highestSeverityOf(args, false);
        Time time = ValueUtil.latestTimeOf(args);
        if (time == null) {
            time = ValueFactory.timeNow();
        }
        return ValueFactory.newVDouble(
                calculate(((VNumber) args.get(0)).getValue().doubleValue(),
                ((VNumber) args.get(1)).getValue().doubleValue())
                , alarm, time, ValueFactory.displayNone());
    }
    
    /**
     * Calculates the result based on the two arguments. This is the only
     * method one has to implement.
     * 
     * @param arg1 the first argument
     * @param arg2 the second argument
     * @return the result
     */
    public abstract double calculate(double arg1, double arg2);
    
}

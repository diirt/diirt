/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.util.Arrays;
import java.util.List;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueFactory;


/**
 * Abstract class for formula functions that take one {@link VNumber} as argument
 * and return a {@code VNumber}.
 * <p>
 * This class takes care of:
 * <ul>
 *    <li>extracting double value from {@code VNumber}</li>
 *    <li>null handling - returns null if the argument is null</li>
 *    <li>alarm handling - returns the argument alarm</li>
 *    <li>time handling - returns the argument time</li>
 *    <li>display handling - returns display none</li>
 * </ul>
 * 
 * @author shroffk
 */
public abstract class AbstractVNumberToVNumberFormulaFunction implements FormulaFunction {

    private final String name;
    private final String description;
    private final List<Class<?>> argumentTypes;
    private final List<String> argumentNames;
    
    public AbstractVNumberToVNumberFormulaFunction(String name, String description, String argName) {
        this.name = name;
        this.description = description;
        this.argumentTypes = Arrays.<Class<?>>asList(VNumber.class);
        this.argumentNames = Arrays.asList(argName);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return argumentTypes;
    }

    @Override
    public List<String> getArgumentNames() {
        return argumentNames;
    }

    @Override
    public Class<?> getReturnType() {
        return VNumber.class;
    }

    @Override
    public final Object calculate(List<Object> args) {
        VNumber arg = (VNumber) args.get(0);
        if (arg == null) {
            return null;
        }
        return ValueFactory.newVDouble(calculate(arg.getValue().doubleValue()),
                arg, arg, ValueFactory.displayNone());
    }
    
    /**
     * Calculates the result based on the arguments. This is the only
     * method one has to implement.
     * 
     * @param arg the argument
     * @return the result
     */
    public abstract double calculate(double arg);
    
}

/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.formula;

import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VNumber;
import org.epics.pvmanager.expression.DesiredRateExpression;
import static org.epics.pvmanager.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    private ExpressionLanguage() {
        // No instances
    }
    
    static DesiredRateExpression<?> cachedPv(String channelName) {
        return new LastOfChannelExpression<Object>(channelName, Object.class);
    }
    
    static DesiredRateExpression<VDouble> add(DesiredRateExpression<? extends VNumber> arg1, DesiredRateExpression<? extends VNumber> arg2) {
        return resultOf(new TwoArgNumericFunction() {

            @Override
            double calculate(double arg1, double arg2) {
                return arg1 + arg2;
            }
        }, arg1, arg2);
    }
    
    static DesiredRateExpression<VDouble> subtract(DesiredRateExpression<? extends VNumber> arg1, DesiredRateExpression<? extends VNumber> arg2) {
        return resultOf(new TwoArgNumericFunction() {

            @Override
            double calculate(double arg1, double arg2) {
                return arg1 - arg2;
            }
        }, arg1, arg2);
    }
    
    static <T> DesiredRateExpression<? extends T> cast(Class<T> clazz, DesiredRateExpression<?> arg1) {
        if (arg1 instanceof LastOfChannelExpression) {
            return ((LastOfChannelExpression<?>)arg1).cast(clazz);
        }
        @SuppressWarnings("unchecked")
        DesiredRateExpression<? extends T> op1 = (DesiredRateExpression<? extends T>) arg1;
        return op1;
    }
    
    static DesiredRateExpression<VDouble> multiply(DesiredRateExpression<? extends VNumber> arg1, DesiredRateExpression<? extends VNumber> arg2) {
        return resultOf(new TwoArgNumericFunction() {

            @Override
            double calculate(double arg1, double arg2) {
                return arg1 * arg2;
            }
        }, arg1, arg2);
    }
    
    static DesiredRateExpression<VDouble> multiplyCast(DesiredRateExpression<?> arg1, DesiredRateExpression<?> arg2) {
        return multiply(cast(VNumber.class, arg1), cast(VNumber.class, arg2));
    }
    
    static DesiredRateExpression<VDouble> divide(DesiredRateExpression<? extends VNumber> arg1, DesiredRateExpression<? extends VNumber> arg2) {
        return resultOf(new TwoArgNumericFunction() {

            @Override
            double calculate(double arg1, double arg2) {
                return arg1 / arg2;
            }
        }, arg1, arg2);
    }
    
    static DesiredRateExpression<VDouble> divideCast(DesiredRateExpression<?> arg1, DesiredRateExpression<?> arg2) {
        return divide(cast(VNumber.class, arg1), cast(VNumber.class, arg2));
    }
    
    static DesiredRateExpression<VDouble> reminder(DesiredRateExpression<? extends VNumber> arg1, DesiredRateExpression<? extends VNumber> arg2) {
        return resultOf(new TwoArgNumericFunction() {

            @Override
            double calculate(double arg1, double arg2) {
                return arg1 % arg2;
            }
        }, arg1, arg2);
    }
    
    static DesiredRateExpression<VDouble> reminderCast(DesiredRateExpression<?> arg1, DesiredRateExpression<?> arg2) {
        return reminder(cast(VNumber.class, arg1), cast(VNumber.class, arg2));
    }
}

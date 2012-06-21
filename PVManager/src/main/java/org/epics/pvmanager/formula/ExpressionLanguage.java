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
    
    static DesiredRateExpression<?> channel(String channelName) {
        return null;
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
    
    static DesiredRateExpression<VDouble> multiply(DesiredRateExpression<? extends VNumber> arg1, DesiredRateExpression<? extends VNumber> arg2) {
        return resultOf(new TwoArgNumericFunction() {

            @Override
            double calculate(double arg1, double arg2) {
                return arg1 * arg2;
            }
        }, arg1, arg2);
    }
}

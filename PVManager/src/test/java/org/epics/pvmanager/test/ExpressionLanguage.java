/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.Function;
import org.epics.pvmanager.data.VInt;
import org.epics.pvmanager.data.ValueFactory;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import static org.epics.pvmanager.data.ValueFactory.*;
import org.epics.pvmanager.expression.DesiredRateExpressionList;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    public static DesiredRateExpression<VInt> counter() {
        return new DesiredRateExpressionImpl<VInt>((DesiredRateExpressionList<?>) null, new Function<VInt>() {
            private int counter = 0;

            @Override
            public VInt getValue() {
                return newVInt(counter++, alarmNone(), timeNow(), displayNone());
            }
        }, "counter");
    }
}

/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.ReadFunction;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.DesiredRateExpressionList;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    
    public static DesiredRateExpression<Integer> counter() {
        return new DesiredRateExpressionImpl<Integer>((DesiredRateExpressionList<?>) null, new ReadFunction<Integer>() {
            private int counter = 0;

            @Override
            public Integer readValue() {
                return counter++;
            }
        }, "counter");
    }
}

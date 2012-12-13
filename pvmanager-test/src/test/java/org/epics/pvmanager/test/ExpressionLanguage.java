/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

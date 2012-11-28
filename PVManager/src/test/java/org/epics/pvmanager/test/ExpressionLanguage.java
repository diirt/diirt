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
import org.epics.pvmanager.data.DataTypeSupport;
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
    
    static {
        DataTypeSupport.install();
    }
    
    public static DesiredRateExpression<VInt> counter() {
        return new DesiredRateExpressionImpl<VInt>((DesiredRateExpressionList<?>) null, new ReadFunction<VInt>() {
            private int counter = 0;

            @Override
            public VInt readValue() {
                return newVInt(counter++, alarmNone(), timeNow(), displayNone());
            }
        }, "counter");
    }
}

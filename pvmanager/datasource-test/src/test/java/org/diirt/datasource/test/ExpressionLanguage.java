/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import org.diirt.datasource.ReadFunction;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import org.diirt.datasource.expression.DesiredRateExpressionList;

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

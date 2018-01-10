/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import org.diirt.graphene.IntensityGraph2DRendererUpdate;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import static org.diirt.datasource.graphene.ExpressionLanguage.functionOf;

/**
 *
 * @author carcassi
 */
public class IntensityGraph2DExpression extends DesiredRateExpressionImpl<Graph2DResult> implements Graph2DExpression<IntensityGraph2DRendererUpdate> {

    IntensityGraph2DExpression(DesiredRateExpression<?> arrayData) {
        super(ExpressionLanguage.<Object>createList(arrayData),
                new IntensityGraph2DFunction(functionOf(arrayData)),
                "Histogram Graph");
    }

    @Override
    public void update(IntensityGraph2DRendererUpdate update) {
        ((IntensityGraph2DFunction) getFunction()).getUpdateQueue().writeValue(update);
    }

    @Override
    public IntensityGraph2DRendererUpdate newUpdate() {
        return new IntensityGraph2DRendererUpdate();
    }
}

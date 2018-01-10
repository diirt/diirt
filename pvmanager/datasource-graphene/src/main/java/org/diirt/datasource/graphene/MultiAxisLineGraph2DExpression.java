/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import org.diirt.graphene.MultiAxisLineGraph2DRendererUpdate;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import static org.diirt.datasource.graphene.ExpressionLanguage.functionOf;

/**
 *
 * @author carcassi
 */
public class MultiAxisLineGraph2DExpression extends DesiredRateExpressionImpl<Graph2DResult> implements Graph2DExpression<MultiAxisLineGraph2DRendererUpdate> {

    MultiAxisLineGraph2DExpression(DesiredRateExpression<?> tableData,
            DesiredRateExpression<?> xColumnName,
            DesiredRateExpression<?> yColumnName) {
        super(ExpressionLanguage.<Object>createList(tableData, xColumnName, yColumnName),
                new MultiAxisLineGraph2DFunction(functionOf(tableData),
                functionOf(xColumnName), functionOf(yColumnName)),
                "Multi-axis Line Graph");
    }

    @Override
    public void update(MultiAxisLineGraph2DRendererUpdate update) {
        ((MultiAxisLineGraph2DFunction) getFunction()).getRendererUpdateQueue().writeValue(update);
    }

    @Override
    public MultiAxisLineGraph2DRendererUpdate newUpdate() {
        return new MultiAxisLineGraph2DRendererUpdate();
    }
}

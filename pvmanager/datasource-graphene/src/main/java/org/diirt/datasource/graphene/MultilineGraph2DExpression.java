/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import org.diirt.graphene.LineGraph2DRendererUpdate;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import static org.diirt.datasource.graphene.ExpressionLanguage.functionOf;

/**
 *
 * @author carcassi
 */
public class MultilineGraph2DExpression extends DesiredRateExpressionImpl<Graph2DResult> implements Graph2DExpression<LineGraph2DRendererUpdate> {

    MultilineGraph2DExpression(DesiredRateExpression<?> tableData,
            DesiredRateExpression<?> xColumnName,
            DesiredRateExpression<?> yColumnName) {
        super(ExpressionLanguage.<Object>createList(tableData, xColumnName, yColumnName),
                new MultilineGraph2DFunction(functionOf(tableData),
                functionOf(xColumnName), functionOf(yColumnName)),
                "Multiline Graph");
    }

    @Override
    public void update(LineGraph2DRendererUpdate update) {
        ((MultilineGraph2DFunction) getFunction()).getRendererUpdateQueue().writeValue(update);
    }

    @Override
    public LineGraph2DRendererUpdate newUpdate() {
        return new LineGraph2DRendererUpdate();
    }
}

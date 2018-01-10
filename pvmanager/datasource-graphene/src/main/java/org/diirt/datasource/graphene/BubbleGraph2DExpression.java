/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import org.diirt.graphene.BubbleGraph2DRendererUpdate;
import org.diirt.datasource.ReadFunction;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import org.diirt.datasource.expression.DesiredRateExpressionList;
import static org.diirt.datasource.graphene.ExpressionLanguage.*;

/**
 * @author shroffk
 *
 */
public class BubbleGraph2DExpression extends DesiredRateExpressionImpl<Graph2DResult> implements Graph2DExpression<BubbleGraph2DRendererUpdate> {

    BubbleGraph2DExpression(DesiredRateExpressionList<?> childExpressions,
            ReadFunction<Graph2DResult> function, String defaultName) {
        super(childExpressions, function, defaultName);
    }

    BubbleGraph2DExpression(DesiredRateExpression<?> tableData,
            DesiredRateExpression<?> xColumnName,
            DesiredRateExpression<?> yColumnName,
            DesiredRateExpression<?> sizeColumnName,
            DesiredRateExpression<?> colorColumnName) {
        super(ExpressionLanguage.<Object>createList(tableData, xColumnName, yColumnName, sizeColumnName, colorColumnName),
                new BubbleGraph2DFunction(functionOf(tableData),
                functionOf(xColumnName), functionOf(yColumnName), functionOf(sizeColumnName), functionOf(colorColumnName)),
                "Bubble Graph");
    }

    @Override
    public void update(BubbleGraph2DRendererUpdate update) {
        ((BubbleGraph2DFunction) getFunction()).getRendererUpdateQueue().writeValue(update);
    }

    @Override
    public BubbleGraph2DRendererUpdate newUpdate() {
        return new BubbleGraph2DRendererUpdate();
    }
}

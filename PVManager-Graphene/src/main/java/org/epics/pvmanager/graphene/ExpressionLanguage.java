/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.graphene;

import org.epics.pvmanager.data.*;
import java.util.List;
import org.epics.pvmanager.BasicTypeSupport;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateExpressionList;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.epics.pvmanager.expression.SourceRateExpressionList;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.DesiredRateExpressionListImpl;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    private ExpressionLanguage() {}

    static {
        // Add support for Epics types.
        DataTypeSupport.install();
        // Add support for Basic types
        BasicTypeSupport.install();
    }

    public static Histogram1DPlot histogramOf(SourceRateExpression<? extends VNumber> vDoubles) {
        DesiredRateExpression<? extends List<? extends VNumber>> queue = newValuesOf(vDoubles);
        return new Histogram1DPlot(queue, new Histogram1DFunction(queue.getFunction()), "histogram");
    }

    public static LineGraphPlot lineGraphOf(SourceRateExpression<VDoubleArray> vDoubleArray) {
        DesiredRateExpression<VDoubleArray> queue = latestValueOf(vDoubleArray);
        return new LineGraphPlot(queue, new LineGraphFunction(queue.getFunction()), "lineGraph");
    }

    public static LineGraphPlot lineGraphOf(SourceRateExpression<VDoubleArray> yArray,
            SourceRateExpression<? extends VNumber> xInitialOffset,
            SourceRateExpression<? extends VNumber> xIncrementSize) {
        DesiredRateExpression<VDoubleArray> yCache = latestValueOf(yArray);
        DesiredRateExpression<? extends VNumber> xInitialOffsetCache = latestValueOf(xInitialOffset);
        DesiredRateExpression<? extends VNumber> xIncrementSizeCache = latestValueOf(xIncrementSize);
        return new LineGraphPlot(new DesiredRateExpressionListImpl<Object>().and(yCache).and(xInitialOffsetCache).and(xIncrementSizeCache),
                new LineGraphFunction(yCache.getFunction(), xInitialOffsetCache.getFunction(), xIncrementSizeCache.getFunction()), "lineGraph");
    }

    public static LineGraphPlot lineGraphOf(SourceRateExpression<VDoubleArray> xVDoubleArray, SourceRateExpression<VDoubleArray> yVDoubleArray) {
        DesiredRateExpression<VDoubleArray> yQueue = latestValueOf(yVDoubleArray);
        DesiredRateExpression<VDoubleArray> xQueue = latestValueOf(xVDoubleArray);
        return new LineGraphPlot(xQueue.and(yQueue), new LineGraphFunction(xQueue.getFunction(), yQueue.getFunction()), "lineGraph");
    }

}

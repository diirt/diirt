/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.extra;

import java.util.List;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.pvmanager.data.VImage;

/**
 * A waterfall plot.
 *
 * @author carcassi
 */
public class WaterfallPlot extends DesiredRateExpressionImpl<VImage> {

    WaterfallPlot(DesiredRateExpression<List<VDoubleArray>> queue, String name) {
        super(queue, new WaterfallPlotFunction2(new DoubleArrayTimeCacheFromVDoubleArray(queue.getFunction()), WaterfallPlotParameters.defaults().internalCopy()), name);
    }
    
    private volatile WaterfallPlotParameters parameters = WaterfallPlotParameters.defaults();

    WaterfallPlotFunction2 getPlotter() {
        return (WaterfallPlotFunction2) getFunction();
    }
    
    /**
     * Changes parameters of the waterfall plot.
     * 
     * @param newParameters parameters to change
     * @return this
     */
    public WaterfallPlot with(WaterfallPlotParameters... newParameters) {
        parameters = new WaterfallPlotParameters(parameters, newParameters);
        WaterfallPlotParameters.InternalCopy copy = parameters.internalCopy();
        getPlotter().setParameters(copy);
        return this;
    }
    
    /**
     * Returns the full set of parameters currently being used.
     * 
     * @return the current parameters
     */
    public WaterfallPlotParameters getParameters() {
        return parameters;
    }
}

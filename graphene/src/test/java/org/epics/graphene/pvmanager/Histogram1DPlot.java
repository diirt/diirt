/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.pvmanager;

import org.epics.pvmanager.Function;
import org.epics.pvmanager.data.VImage;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.DesiredRateExpressionList;

/**
 *
 * @author carcassi
 */
public class Histogram1DPlot extends DesiredRateExpressionImpl<VImage> {

    public Histogram1DPlot(DesiredRateExpressionList<?> childExpressions, Function<VImage> function, String defaultName) {
        super(childExpressions, function, defaultName);
    }
    
}

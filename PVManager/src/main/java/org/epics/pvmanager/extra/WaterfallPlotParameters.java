/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.extra;

import java.awt.Color;

/**
 *
 * @author carcassi
 */
public class WaterfallPlotParameters {

    final int maxHeight;
    final ColorScheme colorScheme;

    public WaterfallPlotParameters() {
        maxHeight = 50;
        colorScheme = ColorScheme.singleRangeGradient(Color.BLACK, Color.WHITE);
    }

    private WaterfallPlotParameters(int maxHeight, ColorScheme colorScheme) {
        this.maxHeight = maxHeight;
        this.colorScheme = colorScheme;
    }

    public WaterfallPlotParameters withMaxHeight(int maxHeight) {
        return new WaterfallPlotParameters(maxHeight, colorScheme);
    }

    public WaterfallPlotParameters withColorScheme(ColorScheme colorScheme) {
        return new WaterfallPlotParameters(maxHeight, colorScheme);
    }

    
}

/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.extra;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author carcassi
 */
public class WaterfallPlotParameters extends Parameters {

    static class InternalCopy {
        final Integer maxHeight;
        final ColorScheme colorScheme;
        final Boolean adaptiveRange;
        
        private InternalCopy(Map<Object, Object> params) {
            maxHeight = (Integer) params.get("maxHeight");
            colorScheme = (ColorScheme) params.get("colorScheme");
            adaptiveRange = (Boolean) params.get("adaptiveRange");
        }
        
    }

    WaterfallPlotParameters(Object name, Object value) {
        super(Collections.singletonMap(name, value));
    }

    WaterfallPlotParameters(Map<Object, Object> map) {
        super(map);
    }

    WaterfallPlotParameters(WaterfallPlotParameters defaults, WaterfallPlotParameters... newValues) {
        super(defaults, newValues);
    }

    static WaterfallPlotParameters defaults() {
        Map<Object, Object> defaults = new HashMap<Object, Object>();
        defaults.put("maxHeight", 50);
        defaults.put("colorScheme", ColorScheme.singleRangeGradient(Color.BLACK, Color.WHITE));
        defaults.put("adaptiveRange", false);
        return new WaterfallPlotParameters(defaults);
    }
    
    InternalCopy internalCopy() {
        return new InternalCopy(getParameters());
    }
    
    public static WaterfallPlotParameters maxHeight(int maxHeight) {
        return new WaterfallPlotParameters("maxHeight", maxHeight);
    }

    public static WaterfallPlotParameters colorScheme(ColorScheme colorScheme) {
        return new WaterfallPlotParameters("colorScheme", colorScheme);
    }

    public static WaterfallPlotParameters adaptiveRange(boolean adaptiveRange) {
        return new WaterfallPlotParameters("adaptiveRange", adaptiveRange);
    }

    
}

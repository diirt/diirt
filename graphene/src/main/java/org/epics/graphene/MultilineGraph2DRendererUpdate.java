/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.util.List;

/**
 *
 * @author asbarber
 * @author jkfeng
 * @author sjdallst
 */
public class MultilineGraph2DRendererUpdate extends Graph2DRendererUpdate<MultilineGraph2DRendererUpdate>{
    private ColorScheme valueColorScheme;
    
    public MultilineGraph2DRendererUpdate valueColorScheme(ColorScheme scheme) {
        this.valueColorScheme = scheme;
        return self();
    }
    
    public ColorScheme getValueColorScheme() {
        return valueColorScheme;
    }
}

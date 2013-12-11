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
    
    /**
     *Set this object's ColorScheme "valueColorScheme" to the given ColorScheme.
     * To be used in combination with the MultilineGraph2DRenderer class and update function.
     * @param scheme Possible schemes:GRAY_SCALE, JET, HOT, COOL, SPRING, BONE, COPPER, PINK
     * @return MultilineGraph2DRendererUpdate
     */
    public MultilineGraph2DRendererUpdate valueColorScheme(ColorScheme scheme) {
        this.valueColorScheme = scheme;
        return self();
    }
    
    /**
     *The current value of this object's ColorScheme variable. Can be null. 
     * @return ColorScheme
     */
    public ColorScheme getValueColorScheme() {
        return valueColorScheme;
    }
}

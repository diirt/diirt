/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class AxisRanges {

    private AxisRanges() {
    }
    
    public static AxisRange axisRange(double min, double max) {
        return new AxisRangeImpl(min, max);
    }
}

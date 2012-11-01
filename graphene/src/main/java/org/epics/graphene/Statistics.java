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
 * The statistics of a given series of numbers. For the purpose of 
 * statistics calulations, NaNs should be skipped. That is,
 * they should not appear as minimum, maximum, average or stdDev, and shouldn't
 * even be included in the count. The number of elements (including NaNs)
 * will be available from the number set itself.
 *
 * @author carcassi
 */
public interface Statistics {
    public int getCount();
    public Number getMinimum();
    public Number getMaximum();
    public double getAverage();
    public double getStdDev();
}

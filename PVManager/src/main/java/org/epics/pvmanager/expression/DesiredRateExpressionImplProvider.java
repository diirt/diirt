/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

/**
 * This interface serves two purposes: gives us the implementation of a
 * DesiredRateExpression, so that we can reach package private implementation
 * methods from multiple implemented interfaces, simulating multiple inheritance;
 * it effectively prevents implementations outside of the package.
 *
 * @author carcassi
 */
interface DesiredRateExpressionImplProvider<T> {
    DesiredRateExpressionImpl<T> getDesiredRateExpressionImpl();
}

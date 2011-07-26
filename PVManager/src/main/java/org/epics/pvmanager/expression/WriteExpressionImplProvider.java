/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

/**
 *
 * @author carcassi
 */
interface WriteExpressionImplProvider<T> {
    WriteExpressionImpl<T> getWriteExpressionImpl();
}

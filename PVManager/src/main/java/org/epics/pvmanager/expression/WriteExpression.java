/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.expression;

import org.epics.pvmanager.WriteBuffer;
import org.epics.pvmanager.WriteFunction;

/**
 * An expression that represent a pv write.
 * Objects of this class are not created directly but through the operators defined
 * in {@link ExpressionLanguage}.
 *
 * @param <T> type taken by the expression
 * @author carcassi
 */
public interface WriteExpression<T> extends WriteExpressionList<T>, WriteExpressionImplProvider<T> {

    public String getName();
    
    public WriteFunction<T> getWriteFunction();
    
    public WriteBuffer createWriteBuffer();
}

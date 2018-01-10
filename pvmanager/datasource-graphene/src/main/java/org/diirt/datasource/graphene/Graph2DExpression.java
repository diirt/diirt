/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.datasource.expression.DesiredRateExpression;

/**
 *
 * @author carcassi
 */
public interface Graph2DExpression<T extends Graph2DRendererUpdate<T>> extends DesiredRateExpression<Graph2DResult> {

    public T newUpdate();

    public void update(T update);

}

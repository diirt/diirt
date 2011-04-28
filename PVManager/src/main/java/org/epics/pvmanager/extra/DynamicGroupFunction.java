/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.epics.pvmanager.Function;

/**
 *
 * @author carcassi
 */
class DynamicGroupFunction extends Function<List<Object>> {
    
    private final List<Function<?>> aguments = new CopyOnWriteArrayList<Function<?>>();

    @Override
    public List<Object> getValue() {
        List<Object> result = new ArrayList<Object>();
        for(Function<?> function : aguments) {
            result.add(function.getValue());
        }
        return result;
    }

    public List<Function<?>> getArguments() {
        return aguments;
    }
    
}

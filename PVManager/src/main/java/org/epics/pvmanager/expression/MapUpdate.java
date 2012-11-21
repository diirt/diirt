/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.epics.pvmanager.Function;

/**
 *
 * @author carcassi
 */
class MapUpdate<T> {
    
    private final Collection<String> expressionsToDelete;
    private final Map<String, Function<T>> expressionsToAdd;
    private final boolean toClear;

    public MapUpdate(Collection<String> expressionsToDelete, Map<String, Function<T>> expressionsToAdd, boolean toClear) {
        this.expressionsToDelete = expressionsToDelete;
        this.expressionsToAdd = expressionsToAdd;
        this.toClear = toClear;
    }

    public Collection<String> getExpressionsToDelete() {
        return expressionsToDelete;
    }

    public Map<String, Function<T>> getExpressionsToAdd() {
        return expressionsToAdd;
    }

    public boolean isToClear() {
        return toClear;
    }
    
    public static <T> MapUpdate<T> clear() {
        return new MapUpdate<>(Collections.<String>emptyList(), Collections.<String, Function<T>>emptyMap(), true);
    }
    
    public static <T> MapUpdate<T> addFunction(String name, Function<T> function) {
        return new MapUpdate<>(Collections.<String>emptyList(), Collections.singletonMap(name, function), false);
    }
    
    public static <T> MapUpdate<T> removeFunction(String name) {
        return new MapUpdate<>(Collections.singleton(name), Collections.<String, Function<T>>emptyMap(), true);
    }
    
}

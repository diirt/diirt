/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author carcassi
 */
public class ValueCache<T> implements Consumer<T>, Supplier<T> {
    
    private T value;

    @Override
    public void accept(T t) {
        this.value = t;
    }

    @Override
    public T get() {
        return value;
    }
    
}

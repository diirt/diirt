/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

/**
 * Handles {@link Cache} singleton.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheFactory {

    public static Cache cache = null;

    public static Cache getCache(CacheConfig defaultConfig) {
        if (cache == null)
            cache = new CacheImpl(defaultConfig);
        return cache;
    }

}

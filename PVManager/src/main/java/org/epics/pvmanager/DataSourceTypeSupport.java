/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class DataSourceTypeSupport {
    protected <A, T extends DataSourceTypeAdapter<? super A,?>> T find(Collection<T> converters, ValueCache<?> cache, A connection) {
        int matched = 0;
        List<T> matchedConverters = new ArrayList<T>();
        for (T converter : converters) {
            int match = converter.match(cache, connection);
            if (match != 0) {
                if (match < matched) {
                    matchedConverters.clear();
                }
                matchedConverters.add(converter);
            }
        }
        
        if (matchedConverters.size() != 1) {
            throw new IllegalStateException(formatMessage(cache, connection, matched, matchedConverters));
        }
        
        return matchedConverters.get(0);
    }
    
    protected String formatMessage(ValueCache<?> cache, Object connection, int match, List<? extends DataSourceTypeAdapter<?, ?>> matchedConverters) {
        if (matchedConverters.isEmpty()) {
            return "DataSource misconfiguration: no match found to convert payload to type. ("
                    + cache.getType() + " - " + connection + ")";
        } else {
            return "DataSource misconfiguration: multiple matches found to convert payload to type. ("
                    + cache.getType() + " - " + connection + ": " + matchedConverters + ")";
        }
    }
}

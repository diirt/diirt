/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Arrays;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class MapOfFunctionTest {
    
    public MapOfFunctionTest() {
    }

    @Test(expected=IllegalArgumentException.class)
    public void duplicatedNames() {
        @SuppressWarnings("unchecked")
        MapOfFunction<Object> map = new MapOfFunction<Object>(Arrays.asList("name", "name"), Arrays.<Function<Object>>asList(null, null));
    }

    @Test(expected=IllegalArgumentException.class)
    public void wrongArgumentNumber() {
        @SuppressWarnings("unchecked")
        MapOfFunction<Object> map = new MapOfFunction<Object>(Arrays.asList("name", "name2"), Arrays.<Function<Object>>asList(null, null, null));
    }
}

/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.service;

import org.epics.pvmanager.service.ServiceMethod;
import java.util.HashMap;
import java.util.Map;
import org.epics.pvmanager.WriteCache;
import org.epics.pvmanager.WriteFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ServiceMethodTest {
    
    public ServiceMethodTest() {
    }

    @Test
    public void execute1() {
        ServiceMethod method = new AddServiceMethod();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", 2);
        WriteCache<Map<String, Object>> cache = new WriteCache<>();
        WriteCache<Exception> exceptionCache = new WriteCache<>();
        method.execute(parameters, cache, exceptionCache);
        
        assertThat(cache.getValue().get("result"), equalTo((Object) 3.0));
        assertThat(exceptionCache.getValue(), nullValue());
    }

    @Test
    public void execute2() {
        ServiceMethod method = new AddServiceMethod();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", "test");
        WriteCache<Map<String, Object>> cache = new WriteCache<>();
        WriteCache<Exception> exceptionCache = new WriteCache<>();
        method.execute(parameters, cache, exceptionCache);
        
        assertThat(cache.getValue(), nullValue());
        assertThat(exceptionCache.getValue(), instanceOf(IllegalArgumentException.class));
    }
}
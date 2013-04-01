/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    public void createServiceMethod() {
        ServiceMethod method = new AddServiceMethod();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", 2);
        WriteCache<Map<String, Object>> cache = new WriteCache<>();
        method.execute(parameters, cache);
        
        assertThat(cache.getValue().get("result"), equalTo((Object) 3.0));
    }
}
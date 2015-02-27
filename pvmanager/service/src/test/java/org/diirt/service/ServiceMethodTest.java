/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.HashMap;
import java.util.Map;
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
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("add");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", 2);
        Map<String, Object> result = method.executeSync(parameters);
        
        assertThat(result.get("result"), equalTo((Object) 3.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute2() {
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("add");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", "test");
        method.executeSync(parameters);
    }

    @Test
    public void execute3() {
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("multiply");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 1);
        parameters.put("arg2", 2);
        Map<String, Object> result = method.executeSync(parameters);
        
        assertThat(result.get("result"), equalTo((Object) 2.0));
    }
    
    @Test
    public void toString1() {
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("multiply");
        assertThat(method.toString(), equalTo("multiply(Number arg1, Number arg2): Number result"));
    }
    
    @Test
    public void toString2() {
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("add");
        assertThat(method.toString(), equalTo("add(Number arg1, Number arg2): Number result"));
    }
}
/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

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

    @Test
    public void executeAsyncVsSync(){
        ServiceMethod method = TimerWaitServiceMethod.createTimerService().getServiceMethods().get("wait");

        // Time measurement
        long startTimeSync, endTimeSync;
        long startTimeAsync, endTimeAsync, postLatchTimeAsync;

        // ASYNC setup
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Map<String, Object>> result = new AtomicReference<>();

        Consumer<Map<String, Object>> callback = (Map<String, Object> newValue) -> {
            result.set(newValue);
            latch.countDown();
        };
        Consumer<Exception> errorCallback = (Exception ex) -> {
            fail("Unexpected async exception");
        };

        // ASYNC execution
        startTimeAsync = System.currentTimeMillis();
        method.executeAsync(new HashMap<>(), callback, errorCallback);
        endTimeAsync = System.currentTimeMillis();

        // await latch
        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            fail("Unable to finish async execution");
        }
        postLatchTimeAsync = System.currentTimeMillis();

        // SYNC execution
        startTimeSync = System.currentTimeMillis();
        method.executeSync(new HashMap<>());
        endTimeSync = System.currentTimeMillis();

        // Test
        assertTrue(endTimeAsync - startTimeAsync < 1000);
        assertTrue(postLatchTimeAsync - startTimeAsync >= 1000);
        assertTrue(endTimeSync - startTimeSync >= 1000);
    }
}
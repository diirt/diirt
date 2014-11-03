/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author carcassi
 */
public class MultiplyServiceMethod extends ServiceMethod {

    public MultiplyServiceMethod() {
        super(new ServiceMethodDescription("multiply", "Multiplies two numbers.")
                .addArgument("arg1", "First argument", Number.class)
                .addArgument("arg2", "Second argument", Number.class)
                .addResult("result", "The product of arg1 and arg2", Number.class));
    }

    @Override
    public void executeMethod(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback) {
        Number arg1 = (Number) parameters.get("arg1");
        Number arg2 = (Number) parameters.get("arg2");
        Number result = arg1.doubleValue() * arg2.doubleValue();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        callback.accept(resultMap);
    }
}

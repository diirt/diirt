/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import java.util.HashMap;
import java.util.Map;
import org.diirt.datasource.WriteFunction;

/**
 *
 * @author carcassi
 */
public class AddServiceMethod extends ServiceMethod {

    public AddServiceMethod() {
        super(new ServiceMethodDescription("add", "Adds two numbers.")
                .addArgument("arg1", "First argument", Number.class)
                .addArgument("arg2", "Second argument", Number.class)
                .addResult("result", "The sum of arg1 and arg2", Number.class));
    }

    @Override
    public void executeMethod(Map<String, Object> parameters, WriteFunction<Map<String, Object>> callback, WriteFunction<Exception> errorCallback) {
        Number arg1 = (Number) parameters.get("arg1");
        Number arg2 = (Number) parameters.get("arg2");
        Number result = arg1.doubleValue() + arg2.doubleValue();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        callback.writeValue(resultMap);
    }
}

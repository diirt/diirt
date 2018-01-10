/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author carcassi
 */
public class MultiplyServiceMethod extends ServiceMethod {

    public MultiplyServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);
    }

    @Override
    public Map<String, Object> syncExecImpl(Map<String, Object> parameters) {
        Number arg1 = (Number) parameters.get("arg1");
        Number arg2 = (Number) parameters.get("arg2");
        Number result = arg1.doubleValue() * arg2.doubleValue();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return resultMap;
    }
}

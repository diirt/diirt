/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.HashMap;
import java.util.Map;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueFactory;

/**
 * An example service method for the multiplication of {@link VNumber}s.
 *
 * @author carcassi
 */
public class MultiplyServiceMethod extends ServiceMethod {

    /**
     * Creates a service method for multiplying {@code VNumber}s together.
     *
     * @param serviceMethodDescription the description of the multiply service
     * method, can't be null
     * @param serviceDescription the description of the math service; can't be
     * null
     */
    public MultiplyServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);
    }

    @Override
    public Map<String, Object> syncExecImpl(Map<String, Object> parameters) {
        // Method implementation: this is what the service method actually does
        // Here we are choosing a synchronous implementation, that is the
        // calculation is done on the thread inside this method call.

        // Extract data from parameters
        VNumber arg1 = (VNumber) parameters.get("arg1");
        VNumber arg2 = (VNumber) parameters.get("arg2");

        // Check for nulls
        if (arg1 == null || arg2 == null) {
            return new HashMap<>();
        }

        // Perform calculation
        VNumber result = ValueFactory.newVDouble(arg1.getValue().doubleValue() * arg2.getValue().doubleValue());

        // Prepare the result
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return resultMap;
    }
}

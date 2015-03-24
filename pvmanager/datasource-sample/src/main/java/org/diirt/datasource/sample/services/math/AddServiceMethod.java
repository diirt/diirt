/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.HashMap;
import java.util.Map;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.Display;
import org.diirt.vtype.Time;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueFactory;

/**
 * An example service method for the addition of {@link VNumber}s.
 *
 * @author carcassi
 * @author asbarber 
 */
public class AddServiceMethod extends ServiceMethod {

    /**
     * Creates a service method for adding {@code VNumber}s together. Service
     * method guarantees immutability.
     *
     * @param serviceMethodDescription the description of the add service
     * method, can't be null
     * @param serviceDescription the description of the math service; can't be
     * null
     */
    public AddServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);
    }

    @Override
    public Map<String, Object> syncExecImpl(Map<String, Object> parameters) {
        // Method implementation: this is what the service method actually does
        
        // Data from parameters
        VNumber arg1 = (VNumber) parameters.get("arg1");
        VNumber arg2 = (VNumber) parameters.get("arg2");
        
        // Operation
        VNumber result = ValueFactory.newVDouble(arg1.getValue().doubleValue() + arg2.getValue().doubleValue());
        
        // Results for caller
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return resultMap;
    }
}

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
import org.diirt.vtype.next.Alarm;
import org.diirt.vtype.next.Display;
import org.diirt.vtype.next.Time;
import org.diirt.vtype.next.VDouble;
import org.diirt.vtype.next.VNumber;

/**
 *
 * @author carcassi
 */
public class AddServiceMethod extends ServiceMethod {

    public AddServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription){
        super(serviceMethodDescription, serviceDescription);
    }
    
    @Override
    public Map<String, Object> syncExecImpl(Map<String, Object> parameters) {
        VNumber arg1 = (VNumber) parameters.get("arg1");
        VNumber arg2 = (VNumber) parameters.get("arg2");
        VNumber result = (VNumber) VDouble.create(
                arg1.getValue().doubleValue() + arg2.getValue().doubleValue(),
                Alarm.noValue(),
                Time.now(),
                Display.none()
        );
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return resultMap;        
    }
}

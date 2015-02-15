/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
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
public class MultiplyServiceMethod extends ServiceMethod {

    public MultiplyServiceMethod() {
        super(new ServiceMethodDescription("multiply", "Multiplies two numbers.")
                .addArgument("arg1", "First argument", VNumber.class)
                .addArgument("arg2", "Second argument", VNumber.class)
                .addResult("result", "The product of arg1 and arg2", VNumber.class));
    }

    //WILL BE REMOVED
    @Override
    public void executeMethod(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback) {
        VNumber arg1 = (VNumber) parameters.get("arg1");
        VNumber arg2 = (VNumber) parameters.get("arg2");
        VNumber result = (VNumber) VDouble.create(
            arg1.getValue().doubleValue() * arg2.getValue().doubleValue(),
            Alarm.noValue(),
            Time.now(),
            Display.none()
        );
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        callback.accept(resultMap);
    }
    
    @Override
    public Map<String, Object> syncExecImpl(Map<String, Object> parameters){
        VNumber arg1 = (VNumber) parameters.get("arg1");
        VNumber arg2 = (VNumber) parameters.get("arg2");
        VNumber result = (VNumber) VDouble.create(
            arg1.getValue().doubleValue() * arg2.getValue().doubleValue(),
            Alarm.noValue(),
            Time.now(),
            Display.none()
        );
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return resultMap;
    }
}

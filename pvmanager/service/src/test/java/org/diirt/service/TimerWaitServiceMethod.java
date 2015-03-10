/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT All rights
 * reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author asbarber
 */
public class TimerWaitServiceMethod extends ServiceMethod {

    public TimerWaitServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);
    }

    @Override
    public Map<String, Object> syncExecImpl(Map<String, Object> parameters) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", "Timer has finished waiting (sleep 0)");
            return resultMap;
    }

    @Override
    public void asyncExecImpl(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback) {
        try {
            Thread.sleep(2000);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", "Timer has finished waiting (sleep 2000)");
            callback.accept(resultMap);
        } catch (InterruptedException ex) {
            errorCallback.accept(ex);
        }
    }

    public static Service createTimerService(){
        return new ServiceDescription("timer", "Simple timer service")
                .addServiceMethod(timerMethod())
                .createService();        
    }
    
    public static ServiceMethodDescription timerMethod() {
        return new ServiceMethodDescription("wait", "Waits a pre-determined time (sync doesn't waits, async wait).") {

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDesccription) {
                return new TimerWaitServiceMethod(this, serviceDesccription);
            }
        };
    }
}

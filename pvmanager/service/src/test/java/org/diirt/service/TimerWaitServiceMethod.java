/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author asbarber
 */
public class TimerWaitServiceMethod extends ServiceMethod {

    public TimerWaitServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);
    }

    @Override
    public Map<String, Object> syncExecImpl(Map<String, Object> parameters) throws Exception{
        try {
            Thread.sleep(1000);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", "Timer has finished waiting (sleep 1000)");
            return resultMap;
        } catch (InterruptedException ex){
            throw new RuntimeException(ex);
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

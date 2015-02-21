/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diirt.service.temp.math;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import org.diirt.service.temp.ServiceDescription;
import org.diirt.service.temp.ServiceMethod;
import org.diirt.service.temp.ServiceMethodDescription;

/**
 *
 * @author Aaron
 */
public class MultiplyServiceMethod extends ServiceMethod{

    public MultiplyServiceMethod(ServiceDescription description) {
        super(new ServiceMethodDescription("multiply", "Multiplies two numbers.")
                .addArgument("arg1", "First argument", Integer.class)
                .addArgument("arg2", "Second argument", Integer.class)
                .addResult("result", "The product of arg1 and arg2", Integer.class),
                description                
        );
    }
    
    @Override
    protected void executeMethod(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback) {
    }
    
    @Override
    public Map<String, Object> syncExecImpl(Map<String, Object> parameters){
        Integer arg1 = (Integer) parameters.get("arg1");
        Integer arg2 = (Integer) parameters.get("arg2");
        Integer result = arg1 * arg2;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return resultMap;
    }    
}

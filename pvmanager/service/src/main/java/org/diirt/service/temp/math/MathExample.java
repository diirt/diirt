/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diirt.service.temp.math;

import java.util.HashMap;
import java.util.Map;
import org.diirt.service.temp.ServiceMethod;

/**
 *
 * @author Aaron
 */
public class MathExample {
 
    public static void main(String[] args) {
        ServiceMethod method = new MathService().getServiceMethods().get("multiply");
        
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", 2);
        parameters.put("arg2", 3);
        System.out.println("service: 2 * 3");

        //Executes the service and obtains all values returned
        Map<String, Object> returnValues = method.executeSync(parameters);

        //Obtains the mathematical results
        Integer result = (Integer) returnValues.get("result");
        System.out.println("result: " + result);        
    }
}

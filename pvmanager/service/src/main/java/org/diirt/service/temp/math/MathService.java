package org.diirt.service.temp.math;

import java.util.concurrent.Executors;
import org.diirt.service.temp.Service;
import org.diirt.service.temp.ServiceDescription;

/**
 *
 * @author Aaron
 */
public class MathService extends Service{
    
    public MathService() {
        super(description());
    }    
    
    private static ServiceDescription description(){
        ServiceDescription description = new ServiceDescription("math", "Simple math service", Executors.newSingleThreadExecutor(org.diirt.util.concurrent.Executors.namedPool("mathService")));
        description.addServiceMethod(new MultiplyServiceMethod(description));
        return description;
    }
}

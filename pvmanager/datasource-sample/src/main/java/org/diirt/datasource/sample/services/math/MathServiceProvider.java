package org.diirt.datasource.sample.services.math;

import java.util.Arrays;
import java.util.Collection;
import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceProvider;

/**
 *
 * @author asbarber
 */
public class MathServiceProvider implements ServiceProvider{

    @Override
    public String getName() {
        return "math";
    }

    @Override
    public Collection<Service> createServices() {
        return Arrays.asList(new MathService());
    }
    
}

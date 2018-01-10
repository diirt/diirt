/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.diirt.service.Service;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ExecServiceProviderTest {

    @Test
    public void new1() throws Exception {
        File file = new File(getClass().getResource(".").toURI());
        ExecServiceProvider factory = new ExecServiceProvider(file, false);
        Collection<Service> services = factory.createServices();
        assertThat(services.size(), equalTo(1));
        Service service = services.iterator().next();
        assertThat(service.getName(), equalTo("execSample"));
        assertThat(service.getDescription(), equalTo("A test service"));
        assertThat(service.getServiceMethods().size(), equalTo(2));
        assertThat(service.getServiceMethods().get("echo").getName(), equalTo("echo"));
        assertThat(service.getServiceMethods().get("echo").getDescription(), equalTo("A test script"));
        assertThat(service.getServiceMethods().get("echo").getArguments().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("echo").getArgumentMap().get("string").getDescription(), equalTo("The string"));
        assertThat(service.getServiceMethods().get("echo").getArgumentMap().get("string").getType(), equalTo((Class) VString.class));
        assertThat(service.getServiceMethods().get("echo").getResults().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("echo").getResultMap().get("result").getType(), equalTo((Class) VType.class));
        assertThat(service.getServiceMethods().get("echo").getResultMap().get("result").getDescription(), equalTo("The result"));
        assertThat(service.getServiceMethods().get("script").getName(), equalTo("script"));
        assertThat(service.getServiceMethods().get("script").getDescription(), equalTo("My script"));
        assertThat(service.getServiceMethods().get("script").getArguments().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("script").getArgumentMap().get("value").getDescription(), equalTo("The value"));
        assertThat(service.getServiceMethods().get("script").getArgumentMap().get("value").getType(), equalTo((Class) VNumber.class));
        assertThat(service.getServiceMethods().get("script").getResults().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("script").getResultMap().get("result").getType(), equalTo((Class) VType.class));
        assertThat(service.getServiceMethods().get("script").getResultMap().get("result").getDescription(), equalTo("The script result"));
    }

    @Test
    public void new2() throws Exception {
        File file = new File("DOES_NOT_EXISTS");
        if (file.exists()) {
            file.delete();
        }
        ExecServiceProvider factory = new ExecServiceProvider(file, false);
        Collection<Service> services = factory.createServices();
        assertThat(services.size(), equalTo(0));
        assertThat(file.exists(), equalTo(true));
        file.delete();
    }

    @Test
    public void runCommand1() throws URISyntaxException, Exception {
        File file = new File(getClass().getResource(".").toURI());
        ExecServiceProvider factory = new ExecServiceProvider(file, false);
        Collection<Service> services = factory.createServices();
        assertThat(services.size(), equalTo(1));
        Service service = services.iterator().next();
        Map<String, Object> params = new HashMap<>();
        params.put("string", ValueFactory.newVString("FOO!", ValueFactory.alarmNone(), ValueFactory.timeNow()));
        Map<String, Object> result = service.getServiceMethods().get("echo").executeSync(params);
        VString output = (VString) result.get("output");
        assertThat(output.getValue(), equalTo("You selected FOO!\n"));
    }

}

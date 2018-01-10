/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.diirt.service.Service;
import org.diirt.vtype.VString;
import org.diirt.vtype.ValueFactory;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class ExecServiceTest {

    @Test
    public void runCommand1() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Service service = new ExecServiceDescription("execSample", "A simple exec service")
                .executorService(executor)
                .addServiceMethod(new ExecServiceMethodDescription("echo", "A simple command")
                .command("echo This is a test!"))
                .createService();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = service.getServiceMethods().get("echo").executeSync(params);
        VString output = (VString) result.get("output");
        assertThat(output.getValue(), equalTo("This is a test!\n"));
    }

    @Test
    public void runCommand2() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Service service = new ExecServiceDescription("execSample", "A simple exec service")
                .executorService(executor)
                .addServiceMethod(new ExecServiceMethodDescription("echo", "A simple command")
                .command("echo You entered #param#")
                .addArgument("param", "The parameter", VString.class))
                .createService();
        Map<String, Object> params = new HashMap<>();
        params.put("param", ValueFactory.newVString("FOO!", ValueFactory.alarmNone(), ValueFactory.timeNow()));
        Map<String, Object> result = service.getServiceMethods().get("echo").executeSync(params);
        VString output = (VString) result.get("output");
        assertThat(output.getValue(), equalTo("You entered FOO!\n"));
    }

}

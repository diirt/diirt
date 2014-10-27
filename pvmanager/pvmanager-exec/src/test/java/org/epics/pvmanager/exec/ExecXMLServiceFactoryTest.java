/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.exec;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.ChannelHandlerWriteSubscription;
import org.epics.pvmanager.WriteFunction;
import org.epics.pvmanager.WriteCache;
import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.ChannelHandlerReadSubscription;
import org.epics.pvmanager.*;
import org.epics.pvmanager.service.Service;
import org.epics.pvmanager.service.ServiceUtil;
import org.epics.vtype.VDouble;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;
import org.epics.vtype.VTable;
import org.epics.vtype.VType;
import org.epics.vtype.ValueFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

/**
 *
 * @author carcassi
 */
public class ExecXMLServiceFactoryTest {

    @Test
    public void new1() throws Exception {
        File file = new File(getClass().getResource(".").toURI());
        ExecXMLServiceFactory factory = new ExecXMLServiceFactory(file);
        Collection<Service> services = factory.createServices();
        assertThat(services.size(), equalTo(1));
        Service service = services.iterator().next();
        assertThat(service.getName(), equalTo("execSample"));
        assertThat(service.getDescription(), equalTo("A test service"));
        assertThat(service.getServiceMethods().size(), equalTo(2));
        assertThat(service.getServiceMethods().get("echo").getName(), equalTo("echo"));
        assertThat(service.getServiceMethods().get("echo").getDescription(), equalTo("A test script"));
        assertThat(service.getServiceMethods().get("echo").getArgumentDescriptions().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("echo").getArgumentDescriptions().get("string"), equalTo("The string"));
        assertThat(service.getServiceMethods().get("echo").getArgumentTypes().get("string"), equalTo((Class) VString.class));
        assertThat(service.getServiceMethods().get("echo").getResultTypes().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("echo").getResultDescriptions().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("echo").getResultTypes().get("result"), equalTo((Class) VType.class));
        assertThat(service.getServiceMethods().get("echo").getResultDescriptions().get("result"), equalTo("The result"));
        assertThat(service.getServiceMethods().get("script").getName(), equalTo("script"));
        assertThat(service.getServiceMethods().get("script").getDescription(), equalTo("My script"));
        assertThat(service.getServiceMethods().get("script").getArgumentDescriptions().get("value"), equalTo("The value"));
        assertThat(service.getServiceMethods().get("script").getArgumentTypes().get("value"), equalTo((Class) VNumber.class));
        assertThat(service.getServiceMethods().get("script").getResultTypes().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("script").getResultDescriptions().size(), equalTo(1));
        assertThat(service.getServiceMethods().get("script").getResultTypes().get("result"), equalTo((Class) VType.class));
        assertThat(service.getServiceMethods().get("script").getResultDescriptions().get("result"), equalTo("The script result"));
    }

    @Test
    public void new2() throws Exception {
        File file = new File("DOES_NOT_EXISTS");
        ExecXMLServiceFactory factory = new ExecXMLServiceFactory(file);
        Collection<Service> services = factory.createServices();
        assertThat(services.size(), equalTo(0));
    }

    @Test
    public void runCommand1() throws URISyntaxException {
        File file = new File(getClass().getResource(".").toURI());
        ExecXMLServiceFactory factory = new ExecXMLServiceFactory(file);
        Collection<Service> services = factory.createServices();
        assertThat(services.size(), equalTo(1));
        Service service = services.iterator().next();
        Map<String, Object> params = new HashMap<>();
        params.put("string", ValueFactory.newVString("FOO!", ValueFactory.alarmNone(), ValueFactory.timeNow()));
        Map<String, Object> result = ServiceUtil.syncExecuteMethod(service.getServiceMethods().get("echo"), params);
        VString output = (VString) result.get("output");
        assertThat(output.getValue(), equalTo("You selected FOO!\n"));
    }

}

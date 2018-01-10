/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import java.io.File;
import java.util.Collection;
import org.diirt.service.Service;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class JDBCServiceProviderTest {

    @Test
    public void new1() throws Exception {
        File file = new File(getClass().getResource(".").toURI());
        JDBCServiceProvider factory = new JDBCServiceProvider(file);
        Collection<Service> services = factory.createServices();
        assertThat(services.size(), equalTo(1));
        Service service = services.iterator().next();
        assertThat(service.getName(), equalTo("jdbcSample"));
        assertThat(service.getDescription(), equalTo("A test service"));
        assertThat(service.getServiceMethods().size(), equalTo(2));
        assertThat(service.getServiceMethods().get("query").getName(), equalTo("query"));
        assertThat(service.getServiceMethods().get("query").getDescription(), equalTo("A test query"));
        assertThat(service.getServiceMethods().get("query").getArguments().size(), equalTo(0));
        assertThat(service.getServiceMethods().get("query").getResultMap().get("result").getType(), equalTo((Class) VTable.class));
        assertThat(service.getServiceMethods().get("query").getResultMap().get("result").getDescription(), equalTo("The query result"));
        assertThat(service.getServiceMethods().get("insert").getName(), equalTo("insert"));
        assertThat(service.getServiceMethods().get("insert").getDescription(), equalTo("A test insert query"));
        assertThat(service.getServiceMethods().get("insert").getResults().size(), equalTo(0));
        assertThat(service.getServiceMethods().get("insert").getArgumentMap().get("name").getDescription(), equalTo("The name"));
        assertThat(service.getServiceMethods().get("insert").getArgumentMap().get("name").getType(), equalTo((Class) VString.class));
        assertThat(service.getServiceMethods().get("insert").getArgumentMap().get("index").getDescription(), equalTo("The index"));
        assertThat(service.getServiceMethods().get("insert").getArgumentMap().get("index").getType(), equalTo((Class) VNumber.class));
        assertThat(service.getServiceMethods().get("insert").getArgumentMap().get("value").getDescription(), equalTo("The value"));
        assertThat(service.getServiceMethods().get("insert").getArgumentMap().get("value").getType(), equalTo((Class) VNumber.class));
    }

    @Test
    public void new2() throws Exception {
        File file = new File("DOES_NOT_EXISTS");
        JDBCServiceProvider factory = new JDBCServiceProvider(file);
        Collection<Service> services = factory.createServices();
        assertThat(services.size(), equalTo(0));
    }

}

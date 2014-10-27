/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.jdbc;

import java.io.InputStream;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.ChannelHandlerWriteSubscription;
import org.epics.pvmanager.WriteFunction;
import org.epics.pvmanager.WriteCache;
import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.ChannelHandlerReadSubscription;
import org.epics.pvmanager.*;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;
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
public class JDBCServicesTest {

    @Test
    public void createFromXml1() {
        InputStream stream = getClass().getResourceAsStream("jdbcService.1.xml");
        JDBCService service = JDBCServices.createFromXml(stream);
        assertThat(service.getName(), equalTo("jdbcSample"));
        assertThat(service.getDescription(), equalTo("A test service"));
        assertThat(service.getServiceMethods().size(), equalTo(2));
        assertThat(service.getServiceMethods().get("query").getName(), equalTo("query"));
        assertThat(service.getServiceMethods().get("query").getDescription(), equalTo("A test query"));
        assertThat(service.getServiceMethods().get("query").getArgumentDescriptions().size(), equalTo(0));
        assertThat(service.getServiceMethods().get("query").getResultTypes().get("result"), equalTo((Class) VTable.class));
        assertThat(service.getServiceMethods().get("query").getResultDescriptions().get("result"), equalTo("The query result"));
        assertThat(service.getServiceMethods().get("insert").getName(), equalTo("insert"));
        assertThat(service.getServiceMethods().get("insert").getDescription(), equalTo("A test insert query"));
        assertThat(service.getServiceMethods().get("insert").getResultTypes().size(), equalTo(0));
        assertThat(service.getServiceMethods().get("insert").getResultDescriptions().size(), equalTo(0));
        assertThat(service.getServiceMethods().get("insert").getArgumentDescriptions().get("name"), equalTo("The name"));
        assertThat(service.getServiceMethods().get("insert").getArgumentTypes().get("name"), equalTo((Class) VString.class));
        assertThat(service.getServiceMethods().get("insert").getArgumentDescriptions().get("index"), equalTo("The index"));
        assertThat(service.getServiceMethods().get("insert").getArgumentTypes().get("index"), equalTo((Class) VNumber.class));
        assertThat(service.getServiceMethods().get("insert").getArgumentDescriptions().get("value"), equalTo("The value"));
        assertThat(service.getServiceMethods().get("insert").getArgumentTypes().get("value"), equalTo((Class) VNumber.class));
    }

}

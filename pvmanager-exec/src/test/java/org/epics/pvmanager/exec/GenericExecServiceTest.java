/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.exec;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.ChannelHandlerWriteSubscription;
import org.epics.pvmanager.WriteFunction;
import org.epics.pvmanager.WriteCache;
import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.ChannelHandlerReadSubscription;
import org.epics.pvmanager.*;
import org.epics.pvmanager.service.ServiceUtil;
import org.epics.vtype.VDouble;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;
import org.epics.vtype.VTable;
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
public class GenericExecServiceTest {

    @Test
    public void runCommand1() {
        GenericExecService services = new GenericExecService();
        Map<String, Object> params = new HashMap<>();
        params.put("command", ValueFactory.newVString("echo This is a test!", ValueFactory.alarmNone(), ValueFactory.timeNow()));
        Map<String, Object> result = ServiceUtil.syncExecuteMethod(services.getServiceMethods().get("run"), params);
        VString output = (VString) result.get("output");
        assertThat(output.getValue(), equalTo("This is a test!\n"));
    }

}

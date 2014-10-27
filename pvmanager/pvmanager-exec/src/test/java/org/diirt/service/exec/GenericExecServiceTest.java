/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import org.diirt.service.exec.GenericExecService;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.diirt.datasource.ValueCache;
import org.diirt.datasource.ChannelHandlerWriteSubscription;
import org.diirt.datasource.WriteFunction;
import org.diirt.datasource.WriteCache;
import org.diirt.datasource.ChannelWriteCallback;
import org.diirt.datasource.ChannelHandlerReadSubscription;
import org.diirt.service.ServiceUtil;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;
import org.diirt.vtype.ValueFactory;
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

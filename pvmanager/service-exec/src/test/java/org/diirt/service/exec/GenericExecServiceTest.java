/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.util.HashMap;
import java.util.Map;

import org.epics.vtype.Alarm;
import org.epics.vtype.Time;
import org.epics.vtype.VString;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class GenericExecServiceTest {

    @Test
    public void runCommand1() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("command", VString.of("echo This is a test!", Alarm.none(), Time.now()));
        Map<String, Object> result = GenericExecService.createGenericExecService().getServiceMethods().get("run").executeSync(params);
        VString output = (VString) result.get("output");
        assertThat(output.getValue(), equalTo("This is a test!\n"));
    }

}

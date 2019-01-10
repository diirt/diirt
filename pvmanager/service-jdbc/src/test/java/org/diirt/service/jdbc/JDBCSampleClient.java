/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceRegistry;
import org.diirt.vtype.io.CSVIO;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VString;
import org.epics.vtype.VTable;

/**
 *
 * @author carcassi
 */
public class JDBCSampleClient {

    public static void main(String[] args) throws Exception {

        ServiceRegistry.getDefault().registerService(JDBCSampleService.create());

        ServiceMethod method;
        VTable table;

        method = ServiceRegistry.getDefault().findServiceMethod("jdbcSample/insert");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("name", VString.of("George", Alarm.none(), Time.now()));
        arguments.put("index", VDouble.of(4.1, Alarm.none(), Time.now(), Display.none()));
        arguments.put("value", VDouble.of(2.11, Alarm.none(), Time.now(), Display.none()));
        method.executeSync(arguments);

        method = ServiceRegistry.getDefault().findServiceMethod("jdbcSample/query");
        table = (VTable) method.executeSync(new HashMap<String, Object>()).get("result");

        CSVIO io = new CSVIO();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);
        io.export(table, outputStreamWriter);
        outputStreamWriter.flush();
    }
}

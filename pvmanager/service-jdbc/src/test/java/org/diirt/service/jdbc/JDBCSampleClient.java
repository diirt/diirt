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
import org.diirt.vtype.VTable;
import org.diirt.vtype.io.CSVIO;
import static org.diirt.vtype.ValueFactory.*;

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
        arguments.put("name", newVString("George", alarmNone(), timeNow()));
        arguments.put("index", newVDouble(4.1, alarmNone(), timeNow(), displayNone()));
        arguments.put("value", newVDouble(2.11, alarmNone(), timeNow(), displayNone()));
        method.executeSync(arguments);

        method = ServiceRegistry.getDefault().findServiceMethod("jdbcSample/query");
        table = (VTable) method.executeSync(new HashMap<String, Object>()).get("result");

        CSVIO io = new CSVIO();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);
        io.export(table, outputStreamWriter);
        outputStreamWriter.flush();
    }
}

/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jdbc;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import org.epics.pvmanager.service.ServiceMethod;
import org.epics.pvmanager.service.ServiceRegistry;
import org.epics.vtype.VTable;
import org.epics.vtype.io.CSVIO;
import static org.epics.vtype.ValueFactory.*;
import static org.epics.pvmanager.service.ServiceUtil.*;

/**
 *
 * @author carcassi
 */
public class JDBCSampleClient {

    public static void main(String[] args) throws Exception {
        
        ServiceRegistry.getDefault().registerService(new JDBCSampleService());
        
        ServiceMethod method;
        VTable table;
        
        method = ServiceRegistry.getDefault().findServiceMethod("jdbcSample/insert");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("name", newVString("George", alarmNone(), timeNow()));
        arguments.put("index", newVDouble(4.1, alarmNone(), timeNow(), displayNone()));
        arguments.put("value", newVDouble(2.11, alarmNone(), timeNow(), displayNone()));
        syncExecuteMethod(method, arguments);
        
        method = ServiceRegistry.getDefault().findServiceMethod("jdbcSample/query");
        table = (VTable) syncExecuteMethod(method, new HashMap<String, Object>()).get("result");
        
        CSVIO io = new CSVIO();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);
        io.export(table, outputStreamWriter);
        outputStreamWriter.flush();
    }
}

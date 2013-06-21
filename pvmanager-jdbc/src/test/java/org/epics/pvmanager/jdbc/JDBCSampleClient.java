/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jdbc;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import org.epics.pvmanager.WriteFunction;
import org.epics.pvmanager.service.ServiceMethod;
import org.epics.pvmanager.service.ServiceRegistry;
import org.epics.vtype.VTable;
import org.epics.vtype.io.CSVIO;
import static org.epics.vtype.ValueFactory.*;

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
        Map<String, Object> arguments = new HashMap<String, Object>();
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
    
    public static Map<String, Object> syncExecuteMethod(ServiceMethod method, Map<String, Object> parameters) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Map<String, Object>> result = new AtomicReference<>();
        final AtomicReference<Exception> exception = new AtomicReference<>();
        method.execute(parameters, new WriteFunction<Map<String, Object>>() {

            @Override
            public void writeValue(Map<String, Object> newValue) {
                result.set(newValue);
                latch.countDown();
            }
        }, new WriteFunction<Exception>() {

            @Override
            public void writeValue(Exception newValue) {
                exception.set(newValue);
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException ex) {
            throw new RuntimeException("Interrupted", ex);
        }
        
        if (result.get() != null) {
            return result.get();
        }
        
        throw new RuntimeException("Failed", exception.get());
    }
}

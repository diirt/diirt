/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jdbc;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.epics.pvmanager.WriteFunction;
import org.epics.pvmanager.service.Service;
import org.epics.pvmanager.service.ServiceMethod;
import org.epics.pvmanager.service.ServiceRegistry;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFactory;
import org.epics.vtype.io.CSVIO;
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class MySQLJDBCService {

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new SimpleDataSource("jdbc:mysql://localhost/test?user=root&password=root");
        ExecutorService executor = Executors.newSingleThreadExecutor(org.epics.pvmanager.util.Executors.namedPool("jdbc"));
        
        Service service = new JDBCServiceDescription("jdbc", "A test service")
                .dataSource(dataSource)
                .executorService(executor)
                .addServiceMethod(new JDBCServiceMethodDescription("test", "A test query")
                    .queryResult("result", "The query result")
                    .query("SELECT * FROM Data"))
                .addServiceMethod(new JDBCServiceMethodDescription("insert", "A test insertquery")
                    .addArgument("name", "The name", VString.class)
                    .addArgument("index", "The index", VNumber.class)
                    .addArgument("value", "The value", VNumber.class)
                    .query("INSERT INTO `test`.`Data` (`Name`, `Index`, `Value`) VALUES (?, ?, ?)"))
                .createService();
        
        ServiceRegistry.getDefault().registerService(service);
        
        ServiceMethod method;
        VTable table;
        
        method = ServiceRegistry.getDefault().findServiceMethod("jdbc/insert");
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("name", newVString("George", alarmNone(), timeNow()));
        arguments.put("index", newVDouble(4.1, alarmNone(), timeNow(), displayNone()));
        arguments.put("value", newVDouble(2.11, alarmNone(), timeNow(), displayNone()));
        syncExecuteMethod(method, arguments);
        
        method = ServiceRegistry.getDefault().findServiceMethod("jdbc/test");
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

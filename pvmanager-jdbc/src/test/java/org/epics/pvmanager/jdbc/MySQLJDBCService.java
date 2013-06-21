/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jdbc;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.epics.pvmanager.WriteFunction;
import org.epics.pvmanager.service.Service;
import org.epics.pvmanager.service.ServiceMethod;
import org.epics.pvmanager.service.ServiceRegistry;
import org.epics.vtype.VTable;
import org.epics.vtype.io.CSVIO;

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
                .createService();
        
        ServiceRegistry.getDefault().registerService(service);
        
        ServiceMethod method = ServiceRegistry.getDefault().findServiceMethod("jdbc/test");

        method.executeMethod(new HashMap<String, Object>(), new WriteFunction<Map<String, Object>>() {
            @Override
            public void writeValue(Map<String, Object> newValue) {
                VTable table = (VTable) newValue.get("result");
                CSVIO io = new CSVIO();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);
                io.export(table, outputStreamWriter);
                try {
                    outputStreamWriter.flush();
                } catch (IOException ex) {
                    Logger.getLogger(MySQLJDBCService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, new WriteFunction<Exception>() {
            @Override
            public void writeValue(Exception newValue) {
                newValue.printStackTrace();
            }
        });

        Thread.sleep(2000);
    }
}

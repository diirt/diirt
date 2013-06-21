/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jdbc;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.WriteFunction;
import org.epics.vtype.VTable;
import org.epics.vtype.io.CSVIO;

/**
 *
 * @author carcassi
 */
public class MySQLJDBCService {
  private static Connection connect = null;
  private static Statement statement = null;
  private static PreparedStatement preparedStatement = null;
  private static ResultSet resultSet = null;
  
    public static void main(String[] args) throws Exception{
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/test?"
                    + "user=root&password=root");


            // PreparedStatements can use variables and are more efficient

            ExecutorService executor = Executors.newSingleThreadExecutor();
            JDBCServiceMethod method = new JDBCServiceMethod(new JDBCServiceMethodDescription("test", "A test query")
                    .queryResult("result", "The query result")
                    .connection(connect)
                    .executorService(executor)
                    .query("SELECT * FROM Data"));
            
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
            executor.shutdown();

        } catch (Exception e) {
            throw e;
        } finally {
            connect.close();
        }
    }
}

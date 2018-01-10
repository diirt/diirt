/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import java.util.concurrent.Executors;
import org.diirt.service.Service;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;

/**
 *
 * @author carcassi
 */
public class JDBCSampleService {

    public static Service create() {
        return new JDBCServiceDescription("jdbcSample", "A test service")
                .dataSource(new SimpleDataSource("jdbc:mysql://localhost/test?user=root&password=root"))
                .executorService(Executors.newSingleThreadExecutor(org.diirt.util.concurrent.Executors.namedPool("jdbcSample")))
                .addServiceMethod(new JDBCServiceMethodDescription("query", "A test query")
                    .query("SELECT * FROM Data")
                    .queryResult("result", "The query result")
                )
                .addServiceMethod(new JDBCServiceMethodDescription("insert", "A test insertquery")
                    .query("INSERT INTO `test`.`Data` (`Name`, `Index`, `Value`, `Time`) VALUES (?, ?, ?, now())")
                    .addArgument("name", "The name", VString.class)
                    .addArgument("index", "The index", VNumber.class)
                    .addArgument("value", "The value", VNumber.class)
                ).createService();
    }

}

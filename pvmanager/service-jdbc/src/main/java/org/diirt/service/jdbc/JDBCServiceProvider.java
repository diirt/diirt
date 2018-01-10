/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import java.io.File;
import java.io.FileInputStream;
import org.diirt.service.AbstractFileServiceProvider;
import org.diirt.service.Service;

/**
 * A service factory that crawls a directory for xml files, and creates
 * a JDBC service from each of them.
 *
 * @author carcassi
 */
public class JDBCServiceProvider extends AbstractFileServiceProvider {

    /**
     * Creates a new factory that reads from the given directory.
     * <p>
     * If the directory does not exist, it creates it and returns an empty set.
     *
     * @param directory a directory
     */
    public JDBCServiceProvider(File directory) {
        super(directory);
    }

    /**
     * Creates a new factory that reads from the default directory.
     */
    public JDBCServiceProvider() {
    }

    @Override
    public Service createService(File file) throws Exception {
        if (file.getName().endsWith(".xml")) {
            return JDBCServices.createFromXml(new FileInputStream(file));
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        return "jdbc";
    }

}

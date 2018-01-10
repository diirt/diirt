/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.io.File;

/**
 * Allows to register for notification for file changes.
 *
 * @author carcassi
 */
interface FileWatcherService {
    /**
     * Registers a file for updates.
     *
     * @param file a file
     * @param callback called when the file changes
     */
    public void addWatcher(File file, Runnable callback);

    /**
     * Unregisters a file for updates.
     *
     * @param file a file
     * @param callback no longer called when the file changes
     */
    public void removeWatcher(File file, Runnable callback);
}

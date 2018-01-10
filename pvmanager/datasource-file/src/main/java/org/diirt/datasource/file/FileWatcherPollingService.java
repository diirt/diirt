/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
public class FileWatcherPollingService implements FileWatcherService {

    private static final Logger log = Logger.getLogger(FileWatcherService.class.getName());

    private final Object lock = new Object();
    private final ScheduledExecutorService exec;
    private final Runnable scanTask = new Runnable() {

        @Override
        public void run() {
            scan();
        }
    };
    private final List<Registration> registrations = new ArrayList<>();

    public FileWatcherPollingService(ScheduledExecutorService exec, Duration scanRate) {
        this.exec = exec;
        exec.scheduleWithFixedDelay(scanTask, 0, scanRate.toNanos(), TimeUnit.NANOSECONDS);
    }

    @Override
    public void addWatcher(File file, Runnable callback) {
        synchronized(lock) {
            try {
                registrations.add(new Registration(file, callback));
            } catch (IOException ex) {
                log.log(Level.WARNING, "Notifications won't be enable for file " + file, ex);
            }
        }
    }

    @Override
    public void removeWatcher(File file, Runnable callback) {
        synchronized(lock) {
            Registration toClose = null;
            for (Registration registration : registrations) {
                if (registration.file.equals(file) && registration.callback.equals(callback)) {
                    toClose = registration;
                }
            }
            registrations.remove(toClose);
        }
    }

    public void scan() {
        synchronized(lock) {
            for (Registration registration : registrations) {
                registration.notifyChanges();
            }
        }
    }

    private class Registration {
        final File file;
        final Runnable callback;
        Long previousTime;

        Registration(File file, Runnable callback) throws IOException {
            this.file = file;
            this.callback = callback;
        }

        void notifyChanges() {
            long latestTime = file.lastModified();
            if ((previousTime == null) || (latestTime != previousTime)) {
                previousTime = latestTime;
                try {
                    callback.run();
                } catch(RuntimeException ex) {
                    // Protecting from callback errors
                    log.log(Level.WARNING, "Exception on the file watcher callback", ex);
                }
            }
        }

    }

}

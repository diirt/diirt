/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.vtype.DataTypeSupport;

/**
 * Data source for locally written data. Each instance of this
 * data source will have its own separate channels and values.
 *
 * @author carcassi
 */
public final class FileDataSource extends DataSource {
    private final static FileFormatRegistry register = FileFormatRegistry.getDefault();
    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(org.diirt.util.concurrent.Executors.namedPool("diirt - file watch"));

    FileDataSource(FileDataSourceConfiguration conf) {
        super(true);
        if (conf.isPollEnabled()) {
            fileWatchService = new FileWatcherPollingService(exec, conf.pollInterval);
        } else {
            fileWatchService = new FileWatcherFileSystemService(exec,
                    Duration.ofSeconds(1));

        }
    }

    private final FileWatcherService fileWatchService;


    FileWatcherService getFileWatchService() {
        return fileWatchService;
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        if (channelName.contains(".")) {
            String fileExt = channelName.substring(
                    channelName.lastIndexOf('.') + 1, channelName.length());
            if (register.contains(fileExt)) {
                return new FileChannelHandler(this, channelName, new File(
                        URI.create("file://" + channelName)),
                        register.getFileFormatFor(fileExt));
            }
        }
        return new FileChannelHandler(this, channelName, new File(
                URI.create("file://" + channelName)), new CSVFileFormat());
    }

    @Override
    public void close() {
        exec.shutdownNow();
        super.close();
    }

}

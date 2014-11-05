/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Executors;

import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.vtype.DataTypeSupport;
import org.diirt.util.time.TimeDuration;

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

    /**
     * Creates a new data source.
     */
    public FileDataSource() {
        this(new FileDataSourceProvider().readDefaultConfiguration());
    }
    
    public FileDataSource(FileDataSourceConfiguration conf) {
        super(true);
        if (conf.isPollEnabled()) {
            fileWatchService = new FileWatcherPollingService(Executors.newSingleThreadScheduledExecutor(org.diirt.datasource.util.Executors.namedPool("diirt - file watch")), conf.pollInterval);
        } else {
            fileWatchService = new FileWatcherFileSystemService(Executors.newSingleThreadScheduledExecutor(org.diirt.datasource.util.Executors.namedPool("diirt - file watch")),
                    TimeDuration.ofSeconds(1.0));
            
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
    
}

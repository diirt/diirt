/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Map.Entry;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A data source that can dispatch a request to multiple different
 * data sources.
 *
 * @author carcassi
 */
public class CompositeDataSource extends DataSource {
    
    private static Logger log = Logger.getLogger(CompositeDataSource.class.getName());

    // Stores all data sources by name
    private Map<String, DataSource> dataSources = new ConcurrentHashMap<String, DataSource>();

    private volatile String delimiter = "://";
    private volatile String defaultDataSource;

    /**
     * Creates a new CompositeDataSource.
     */
    public CompositeDataSource() {
        super(true);
    }

    /**
     * Returns the delimeter that divides the data source name from the
     * channel name. Default is "://" so that "epics://pv1" corresponds
     * to the "pv1" channel from the "epics" datasource.
     *
     * @return data source delimeter; can't be null
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Changes the data source delimiter.
     *
     * @param delimiter new data source delimiter; can't be null
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Adds/replaces the data source corresponding to the given name.
     *
     * @param name the name of the data source
     * @param dataSource the data source to add/replace
     */
    public void putDataSource(String name, DataSource dataSource) {
        dataSources.put(name, dataSource);
    }

    /**
     * Returns which data source is used if no data source is specified in the
     * channel name.
     *
     * @return the default data source, or null if it was never set
     */
    public String getDefaultDataSource() {
        return defaultDataSource;
    }
    
    /**
     * Returns the data sources registered to this composite data source.
     * 
     * @return the registered data sources
     */
    public Map<String, DataSource> getDataSources() {
        return Collections.unmodifiableMap(dataSources);
    }

    /**
     * Sets the data source to be used if the channel does not specify
     * one explicitely. The data source must have already been added.
     *
     * @param defaultDataSource the default data source
     */
    public void setDefaultDataSource(String defaultDataSource) {
        if (!dataSources.containsKey(defaultDataSource))
            throw new IllegalArgumentException("The data source " + defaultDataSource + " was not previously added, and therefore cannot be set as default");

        this.defaultDataSource = defaultDataSource;
    }

    // Need to remember how the recipes where split, so that they can be
    // re-sent for disconnection
    private Map<ReadRecipe, Map<String, ReadRecipe>> splitRecipes =
            new ConcurrentHashMap<ReadRecipe, Map<String, ReadRecipe>>();
    private Map<WriteRecipe, Map<String, WriteRecipe>> writeBuffers =
            new ConcurrentHashMap<WriteRecipe, Map<String, WriteRecipe>>();
    
    private String nameOf(String channelName) {
        int indexDelimiter = channelName.indexOf(delimiter);
        if (indexDelimiter == -1) {
            return channelName;
        } else {
            return channelName.substring(indexDelimiter + delimiter.length());
        }
    }
    
    private String sourceOf(String channelName) {
        int indexDelimiter = channelName.indexOf(delimiter);
        if (indexDelimiter == -1) {
            if (defaultDataSource == null)
                throw new IllegalArgumentException("Channel " + channelName + " uses the default data source but one was never set.");
            return defaultDataSource;
        } else {
            String source = channelName.substring(0, indexDelimiter);
            if (dataSources.containsKey(source))
                return source;
            throw new IllegalArgumentException("Data source " + source + " for " + channelName + " was not configured.");
        }
    }

    @Override
    public void connectRead(ReadRecipe recipe) {
        Map<String, ReadRecipe> splitRecipe = new HashMap<String, ReadRecipe>();

        // Iterate through the recipe to understand how to distribute
        // the calls
        Map<String, Collection<ChannelReadRecipe>> routingRecipes = new HashMap<String, Collection<ChannelReadRecipe>>();
        for (ChannelReadRecipe channelRecipe : recipe.getChannelReadRecipes()) {
            String name = nameOf(channelRecipe.getChannelName());
            String dataSource = sourceOf(channelRecipe.getChannelName());

            if (dataSource == null)
                throw new IllegalArgumentException("Channel " + name + " uses the default data source but one was never set.");

            // Add recipe for the target dataSource
            if (routingRecipes.get(dataSource) == null) {
                routingRecipes.put(dataSource, new HashSet<ChannelReadRecipe>());
            }
            routingRecipes.get(dataSource).add(new ChannelReadRecipe(name, channelRecipe.getReadSubscription()));
        }
        
        // Create the recipes
        for (Entry<String, Collection<ChannelReadRecipe>> entry : routingRecipes.entrySet()) {
            splitRecipe.put(entry.getKey(), new ReadRecipe(entry.getValue()));
        }

        splitRecipes.put(recipe, splitRecipe);

        // Dispatch calls to all the data sources
        for (Map.Entry<String, ReadRecipe> entry : splitRecipe.entrySet()) {
            try {
                DataSource dataSource = dataSources.get(entry.getKey());
                if (dataSource == null)
                    throw new IllegalArgumentException("DataSource '" + entry.getKey() + "://' was not configured.");
                dataSource.connectRead(entry.getValue());
            } catch (RuntimeException ex) {
                // If data source fail, still go and connect the others
                recipe.getChannelReadRecipes().iterator().next().getReadSubscription().getExceptionWriteFunction().setValue(ex);
            }
        }
    }

    @Override
    public void disconnectRead(ReadRecipe recipe) {
        Map<String, ReadRecipe> splitRecipe = splitRecipes.get(recipe);
        if (splitRecipe == null) {
            log.log(Level.WARNING, "DataRecipe {0} was disconnected but was never connected. Ignoring it.", recipe);
            return;
        }

        // Dispatch calls to all the data sources
        for (Map.Entry<String, ReadRecipe> entry : splitRecipe.entrySet()) {
            try {
                dataSources.get(entry.getKey()).disconnectRead(entry.getValue());
            } catch(RuntimeException ex) {
                // If a data source fails, still go and disconnect the others
                recipe.getChannelReadRecipes().iterator().next().getReadSubscription().getExceptionWriteFunction().setValue(ex);
            }
        }

        splitRecipes.remove(recipe);
    }

    @Override
    public void connectWrite(WriteRecipe writeBuffer) {
        // Chop the buffer along different data sources
        Map<String, Collection<ChannelWriteRecipe>> buffers = new HashMap<String, Collection<ChannelWriteRecipe>>();
        for (ChannelWriteRecipe channelWriteBuffer : writeBuffer.getChannelWriteBuffers()) {
            String channelName = nameOf(channelWriteBuffer.getChannelName());
            String dataSource = sourceOf(channelWriteBuffer.getChannelName());
            Collection<ChannelWriteRecipe> buffer = buffers.get(dataSource);
            if (buffer == null) {
                buffer = new ArrayList<ChannelWriteRecipe>();
                buffers.put(dataSource, buffer);
            }
            buffer.add(new ChannelWriteRecipe(channelName, channelWriteBuffer.getWriteSubscription()));
        }
        
        Map<String, WriteRecipe> splitBuffers = new HashMap<String, WriteRecipe>();
        for (Map.Entry<String, Collection<ChannelWriteRecipe>> en : buffers.entrySet()) {
            String dataSource = en.getKey();
            Collection<ChannelWriteRecipe> val = en.getValue();
            WriteRecipe newWriteBuffer = new WriteRecipe(val);
            splitBuffers.put(dataSource, newWriteBuffer);
            dataSources.get(dataSource).connectWrite(newWriteBuffer);
        }
        
        writeBuffers.put(writeBuffer, splitBuffers);
    }

    @Override
    public void disconnectWrite(WriteRecipe writeBuffer) {
        Map<String, WriteRecipe> splitBuffer = writeBuffers.remove(writeBuffer);
        if (splitBuffer == null) {
            log.log(Level.WARNING, "WriteBuffer {0} was unregistered but was never registered. Ignoring it.", writeBuffer);
            return;
        }
        
        for (Map.Entry<String, WriteRecipe> en : splitBuffer.entrySet()) {
            String dataSource = en.getKey();
            WriteRecipe splitWriteBuffer = en.getValue();
            dataSources.get(dataSource).disconnectWrite(splitWriteBuffer);
        }
    }
    

    @Override
    ChannelHandler channel(String channelName) {
        String name = nameOf(channelName);
        String dataSource = sourceOf(channelName);
        return dataSources.get(dataSource).channel(name);
    }
    
    @Override
    protected ChannelHandler createChannel(String channelName) {
        throw new UnsupportedOperationException("Composite data source can't create channels directly.");
    }

    /**
     * Closes all DataSources that are registered in the composite.
     */
    @Override
    public void close() {
        for (DataSource dataSource : dataSources.values()) {
            dataSource.close();
        }
    }

    @Override
    public Map<String, ChannelHandler> getChannels() {
        Map<String, ChannelHandler> channels = new HashMap<String, ChannelHandler>();
        for (Entry<String, DataSource> entry : dataSources.entrySet()) {
            String dataSourceName = entry.getKey();
            DataSource dataSource = entry.getValue();
            for (Entry<String, ChannelHandler> channelEntry : dataSource.getChannels().entrySet()) {
                String channelName = channelEntry.getKey();
                ChannelHandler channelHandler = channelEntry.getValue();
                channels.put(dataSourceName + delimiter + channelName, channelHandler);
            }
        }
        
        return channels;
    }

}

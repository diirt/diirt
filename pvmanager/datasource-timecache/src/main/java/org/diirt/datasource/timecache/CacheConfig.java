/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.diirt.datasource.timecache.source.DataSource;
import org.diirt.datasource.timecache.storage.DataStorage;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class CacheConfig {

    private List<DataSource> sources = new ArrayList<DataSource>();
    private DataStorage storage;
    private int nbOfChunksPerQuery = 100;
    private Duration retrievalGap = Duration.ofHours(168); // 1 week

    public void addSource(DataSource ds) {
        sources.add(ds);
    }

    public List<DataSource> getSources() {
        return sources;
    }

    public DataStorage getStorage() {
        return storage;
    }

    public void setStorage(DataStorage storage) {
        this.storage = storage;
    }

    public int getNbOfChunksPerQuery() {
        return nbOfChunksPerQuery;
    }

    public void setNbOfChunksPerQuery(int nbOfChunksPerQuery) {
        this.nbOfChunksPerQuery = nbOfChunksPerQuery;
    }

    public Duration getRetrievalGap() {
        return retrievalGap;
    }

    public void setRetrievalGap(Duration retrievalGap) {
        this.retrievalGap = retrievalGap;
    }

}

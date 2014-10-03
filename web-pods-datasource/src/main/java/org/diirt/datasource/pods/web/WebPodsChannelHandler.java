/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.MultiplexedChannelHandler;
import org.epics.vtype.VType;
import org.epics.vtype.ValueFactory;

/**
 * Implementation for channels of a {@link LocalDataSource}.
 *
 * @author carcassi
 */
class WebPodsChannelHandler extends MultiplexedChannelHandler<Object, Object> {
    
    private final WebPodsDataSource dataSource;

    WebPodsChannelHandler(WebPodsDataSource dataSource, String channelName) {
        super(channelName);
        this.dataSource = dataSource;
    }
    
    @Override
    public void connect() {
        processConnection(null);
    }

    @Override
    public void disconnect() {
        processConnection(null);
    }

    @Override
    protected boolean isConnected(Object payload) {
        return true;
    }

    @Override
    protected boolean isWriteConnected(Object payload) {
        return true;
    }

    @Override
    protected void write(Object newValue, ChannelWriteCallback callback) {
    }

}

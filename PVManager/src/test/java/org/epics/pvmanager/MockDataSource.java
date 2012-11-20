/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

/**
 *
 * @author carcassi
 */
public class MockDataSource extends DataSource {
    
    private volatile ReadRecipe readRecipe;
    private volatile WriteRecipe writeRecipe;

    public MockDataSource() {
        super(true);
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connectRead(ReadRecipe recipe) {
        this.readRecipe = recipe;
    }

    @Override
    public void disconnectRead(ReadRecipe recipe) {
        this.readRecipe = recipe;
    }

    @Override
    public void connectWrite(WriteRecipe writeRecipe) {
        this.writeRecipe = writeRecipe;
    }

    @Override
    public void disconnectWrite(WriteRecipe writeRecipe) {
        this.writeRecipe = writeRecipe;
    }

    public ReadRecipe getReadRecipe() {
        return readRecipe;
    }

    public WriteRecipe getWriteRecipe() {
        return writeRecipe;
    }
    
}

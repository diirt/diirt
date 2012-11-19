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
    
    private ReadRecipe dataRecipe;
    private WriteRecipe writeBuffer;

    public MockDataSource() {
        super(true);
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connect(ReadRecipe recipe) {
        this.dataRecipe = recipe;
    }

    @Override
    public void disconnect(ReadRecipe recipe) {
        this.dataRecipe = recipe;
    }

    @Override
    public void prepareWrite(WriteRecipe writeBuffer) {
        this.writeBuffer = writeBuffer;
    }

    @Override
    public void concludeWrite(WriteRecipe writeBuffer) {
        this.writeBuffer = writeBuffer;
    }

    public ReadRecipe getDataRecipe() {
        return dataRecipe;
    }

    public WriteRecipe getWriteBuffer() {
        return writeBuffer;
    }
    
}

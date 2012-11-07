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
    
    private DataRecipe dataRecipe;
    private WriteBuffer writeBuffer;

    public MockDataSource() {
        super(true);
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connect(DataRecipe recipe) {
        this.dataRecipe = recipe;
    }

    @Override
    public void disconnect(DataRecipe recipe) {
        this.dataRecipe = recipe;
    }

    @Override
    public void prepareWrite(WriteBuffer writeBuffer, ExceptionHandler exceptionHandler) {
        this.writeBuffer = writeBuffer;
    }

    @Override
    public void concludeWrite(WriteBuffer writeBuffer, ExceptionHandler exceptionHandler) {
        this.writeBuffer = writeBuffer;
    }

    public DataRecipe getDataRecipe() {
        return dataRecipe;
    }

    public WriteBuffer getWriteBuffer() {
        return writeBuffer;
    }
    
}

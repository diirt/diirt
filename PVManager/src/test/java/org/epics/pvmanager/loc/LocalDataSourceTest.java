/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.loc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.epics.pvmanager.ChannelHandlerWriteSubscription;
import org.epics.pvmanager.ChannelWriteBuffer;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.DataRecipeBuilder;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVWriterListener;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.WriteBuffer;
import org.epics.pvmanager.WriteCache;
import org.epics.pvmanager.data.VDouble;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class LocalDataSourceTest {

    public LocalDataSourceTest() {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Mock WriteBuffer writeBuffer;
    @Mock DataRecipe dataRecipe;
    @Mock WriteCache<?> writeCache1;
    @Mock WriteCache<?> writeCache2;
    @Mock Runnable callback;
    @Mock ExceptionHandler exceptionHandler;
    @Mock ValueCache<VDouble> valueCache1;
    @Mock ValueCache<VDouble> valueCache2;
    @Mock Collector collector;
    @Mock PVWriterListener<Object> listener;
    String channelName1 = "test1";
    String channelName2 = "test2";

    @Test
    public void writeToLocalDataSource() throws Exception {
        // Prepare mock write buffer
        {
            Collection<ChannelWriteBuffer> channelWriteBuffers = new ArrayList<ChannelWriteBuffer>();
            //Map<String, WriteCache<?>> caches = new HashMap<String, WriteCache<?>>();
            //caches.put(channelName1, writeCache1);
            when(writeCache1.getValue()).thenReturn(6.28);
            channelWriteBuffers.add(new ChannelWriteBuffer(channelName1, new ChannelHandlerWriteSubscription(writeCache1, exceptionHandler, null, null)));
            //caches.put(channelName2, writeCache2);
            when(writeCache2.getValue()).thenReturn(16.28);
            channelWriteBuffers.add(new ChannelWriteBuffer(channelName2, new ChannelHandlerWriteSubscription(writeCache2, exceptionHandler, null, null)));
            //when(writeBuffer.getWriteCaches()).thenReturn(caches);
            when(writeBuffer.getChannelWriteBuffers()).thenReturn(channelWriteBuffers);
        }
        
        DataRecipe recipe;
        
        // Prepare mock read recipe
        {
            Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
            caches.put(channelName1, valueCache1);
            caches.put(channelName2, valueCache2);
            recipe = new DataRecipe(exceptionHandler);
            recipe = recipe.includeCollector(collector, caches);
        }
        
        // TEST: connect, write, disconnect
        LocalDataSource dataSource = new LocalDataSource();
        dataSource.connect(recipe);
        dataSource.prepareWrite(writeBuffer, exceptionHandler);
        dataSource.write(writeBuffer, callback, exceptionHandler);
        Thread.sleep(200);
        dataSource.concludeWrite(writeBuffer, exceptionHandler);
        dataSource.disconnect(recipe);
        
        // Check that the correct value was written and that the write notification was sent
        ArgumentCaptor<VDouble> newValue1 = ArgumentCaptor.forClass(VDouble.class); 
        ArgumentCaptor<VDouble> newValue2 = ArgumentCaptor.forClass(VDouble.class); 
        verify(valueCache1, times(2)).setValue(newValue1.capture());
        assertThat(newValue1.getAllValues().get(0).getValue(), equalTo(0.0));
        assertThat(newValue1.getAllValues().get(1).getValue(), equalTo(6.28));
        verify(valueCache2, times(2)).setValue(newValue2.capture());
        assertThat(newValue2.getAllValues().get(0).getValue(), equalTo(0.0));
        assertThat(newValue2.getAllValues().get(1).getValue(), equalTo(16.28));
        verify(collector, times(4)).collect();
        verify(callback).run();
    }
    
    //@Test
    public void fullSyncPipeline() throws Exception {
        LocalDataSource dataSource = new LocalDataSource();
        PVReader<Object> pv = PVManager.read(channel(channelName1)).from(dataSource).maxRate(ofHertz(100));
        PVWriter<Object> writer = PVManager.write(channel(channelName1)).from(dataSource).sync();
        writer.addPVWriterListener(listener);
        writer.write(10);
        
        verify(listener).pvChanged(null);
        Thread.sleep(50);
        pv.close();
        writer.close();
        
        assertThat(((VDouble) pv.getValue()).getValue(), equalTo(10.0));
    }
    
    //@Test
    public void fullSyncPipelineWithTwoDataSources() throws Exception {
        LocalDataSource dataSource1 = new LocalDataSource();
        LocalDataSource dataSource2 = new LocalDataSource();
        CompositeDataSource compositeSource = new CompositeDataSource();
        compositeSource.putDataSource("loc1", dataSource1);
        compositeSource.putDataSource("loc2", dataSource2);
        PVReader<Object> pv1 = PVManager.read(channel("loc1://test")).from(compositeSource).maxRate(ofHertz(100));
        PVReader<Object> pv2 = PVManager.read(channel("loc2://test")).from(compositeSource).maxRate(ofHertz(100));
        PVWriter<Object> writer = PVManager.write(channel("loc1://test")).from(compositeSource).sync();
        writer.addPVWriterListener(listener);
        writer.write(10);
        
        verify(listener).pvChanged(null);
        Thread.sleep(50);
        pv1.close();
        writer.close();
        
        assertThat(((VDouble) pv1.getValue()).getValue(), equalTo(10.0));
    }
    
    //@Test
    public void fullAsyncPipeline() throws Exception {
        LocalDataSource dataSource = new LocalDataSource();
        PVReader<Object> pv = PVManager.read(channel(channelName1)).from(dataSource).maxRate(ofHertz(100));
        PVWriter<Object> writer = PVManager.write(channel(channelName1)).from(dataSource).async();
        writer.addPVWriterListener(listener);
        writer.write(10);
        // On some machines, the local write is fast enough that it may already
        // be done
        verify(listener, atMost(1)).pvChanged(null);
        
        Thread.sleep(50);
        pv.close();
        writer.close();
        
        verify(listener).pvChanged(null);
        assertThat(((VDouble) pv.getValue()).getValue(), equalTo(10.0));
    }

    @Test
    public void writeMultipleNamesForSameChannel() throws Exception {
        
        DataRecipe recipe = mapOf(latestValueOf(channels("foo", "foo(2.0)"))).getDataRecipe();
        
        // Connect and make sure only one channel is created
        LocalDataSource dataSource = new LocalDataSource();
        dataSource.connect(recipe);
        assertThat(dataSource.getChannels().size(), equalTo(1));
        dataSource.disconnect(recipe);
        dataSource.close();
    }

}

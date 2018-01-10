/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.rpcservice;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.service.Service;
import org.diirt.support.pva.rpcservice.rpcclient.PoolConfiguration;
import org.diirt.support.pva.rpcservice.rpcclient.PoolStatistics;
import org.diirt.support.pva.rpcservice.rpcclient.PooledRPCClientFactory;
import org.epics.pvaccess.PVAException;
import org.epics.pvaccess.client.rpc.RPCClient;
import org.epics.pvaccess.server.rpc.RPCRequestException;
import org.epics.pvaccess.server.rpc.RPCServer;
import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testing a RPC client pooling
 *
 * @author dkumar
 */
// TODO
@Ignore
public class PooledRPCClientTest implements Runnable {

  //testing channel name
  private static final String TEST_CHANNEL = "testChannel";

  //a testing rpc server that implements a simple sum method functionality
  private RPCServer rpcServer;

  //a utility member for pv request creation
  private final static FieldCreate fieldCreate = FieldFactory.getFieldCreate();


  @Test
  public void testServiceWithAPoolConfiguration() {

    InputStream stream = getClass().getResourceAsStream("RPCSumServiceWithPoolConfiguration.xml");

    //the rpcservice will read the pool configuration from the rpcservice description xml and set it on the PooledRPCClientFactory
    Service service = RPCServices.createFromXml(stream);

    PoolConfiguration poolConfiguration = PooledRPCClientFactory.getPoolConfiguration();

    //asserting the values that are different from the default configuration

    assertThat(poolConfiguration.getAbandonWhenPercentageFull(), equalTo(11));
    assertThat(poolConfiguration.getInitialSize(), equalTo(5));
    assertThat(poolConfiguration.isLogAbandoned(), equalTo(true));
    assertThat(poolConfiguration.getMaxActive(), equalTo(6));
    assertThat(poolConfiguration.getMaxIdle(), equalTo(3));
    assertThat(poolConfiguration.getMinIdle(), equalTo(1));
    assertThat(poolConfiguration.getMaxWait(), equalTo(5000));
    assertThat(poolConfiguration.getMinEvictableIdleTimeMillis(), equalTo(35000));
    assertThat(poolConfiguration.isRemoveAbandoned(), equalTo(true));
    assertThat(poolConfiguration.getRemoveAbandonedTimeout(), equalTo(1234));
    assertThat(poolConfiguration.isTestOnBorrow(), equalTo(true));
    assertThat(poolConfiguration.isTestOnReturn(), equalTo(true));

    assertThat(poolConfiguration.isTestWhileIdle(), equalTo(true));
    assertThat(poolConfiguration.getTimeBetweenEvictionRunsMillis(), equalTo(2345));
    assertThat(poolConfiguration.getValidationInterval(), equalTo(60000l));
    assertThat(poolConfiguration.getMaxAge(), equalTo(123l));
    assertThat(poolConfiguration.getUseLock(), equalTo(true));
    assertThat(poolConfiguration.getSuspectTimeout(), equalTo(100));

  }


  @Test
  public void testConfigurePoolWithWrongMaxActive() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();
    poolConfig.setMaxActive(-1);

    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);

    assertTrue("pool size should be DEFAULT_MAX_ACTIVE, despite the wrong configuration",
      poolConfig.getMaxActive() == PoolConfiguration.DEFAULT_MAX_ACTIVE );

    rpcClient.destroy();
  }


  @Test
  public void testConfigurePoolWithWrongMaxActiveAndInitialSize() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();
    poolConfig.setMaxActive(10);
    poolConfig.setInitialSize(100);

    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);

    assertTrue("pool initial size should be equal to pool max active, despite the wrong configuration",
      poolConfig.getInitialSize() == poolConfig.getMaxActive() );

    rpcClient.destroy();
  }


  @Test
  public void testConfigurePoolWithWrongMinIdleAndMaxActive() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();
    poolConfig.setMaxActive(10);
    poolConfig.setMinIdle(100);

    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);

    assertTrue("pool min idle size should be equal to pool max active, despite the wrong configuration",
      poolConfig.getMinIdle() == poolConfig.getMaxActive() );

    rpcClient.destroy();
  }


  @Test
  public void testConfigurePoolWithWrongMinIdleAndMaxIdle() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();
    poolConfig.setMaxIdle(10);
    poolConfig.setMinIdle(100);

    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);

    assertTrue("pool min idle size should be equal to max idle, despite the wrong configuration",
      poolConfig.getMinIdle() == poolConfig.getMaxIdle() );

    rpcClient.destroy();
  }


  @Test
  public void testGetRpcClientsFromOnePool() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();
    poolConfig.setInitialSize(2);

    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);
    PoolStatistics stat = PooledRPCClientFactory.getPoolStatistics(null, TEST_CHANNEL);
    assertNotNull("statistics for pool not obtained", stat);

    assertThat(stat.getChannelName(), equalTo(TEST_CHANNEL));
    assertNull("rpc client's hostname should be null", stat.getHostName());
    assertThat(stat.getActive(), equalTo(1));
    assertThat(stat.getSize(), equalTo(2));
    assertThat(stat.getName(), containsString("RPCClientPool"));
    assertThat(stat.getName(), containsString(TEST_CHANNEL));
    assertThat(stat.getWaitCount(), equalTo(0));
    RPCClient anotherRpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);
    assertThat(stat.getActive(), equalTo(2));

    rpcClient.destroy();
    anotherRpcClient.destroy();

    assertThat(stat.getActive(), equalTo(0));
    assertThat(stat.getIdle(), equalTo(2));

  }


  @Test
  public void testGetRpcClientsFromTwoPools() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();
    poolConfig.setMaxActive(2);

    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient("localhost", TEST_CHANNEL);
    PoolStatistics stat = PooledRPCClientFactory.getPoolStatistics("localhost", TEST_CHANNEL);
    assertNotNull("statistics for a pool not obtained", stat);

    assertThat(stat.getChannelName(), equalTo(TEST_CHANNEL));
    assertThat(stat.getHostName(), equalTo("localhost"));

    assertThat(stat.getActive(), equalTo(1));
    assertThat(stat.getSize(), equalTo(2));

    //get a rpc client from a different pool
    RPCClient anotherRpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);
    PoolStatistics anotherStat = PooledRPCClientFactory.getPoolStatistics(null, TEST_CHANNEL);
    assertNotNull("statistics for another pool not obtained", anotherStat);
    assertTrue("statistics for two different pools should not be the same", !(anotherStat == stat));

    assertNull("another's pool hostname should be null", anotherStat.getHostName());

    rpcClient.destroy();
    anotherRpcClient.destroy();
  }


  @Test(expected = RPCRequestException.class)
  public void testGetRpcClientFromPoolWithoutIdleSlots() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();
    poolConfig.setInitialSize(1);
    poolConfig.setMaxActive(1);
    //wait for 2 seconds for a pooled rpc client
    poolConfig.setMaxWait(2000);

    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);
    RPCClient anotherRpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);

    assertTrue("we should not obtain the second rpc client since the pool should have no idle slots free", false);
  }


  @Test
  public void testValidationOnIdleRpcClients() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();

    poolConfig.setInitialSize(1);
    poolConfig.setMaxActive(1);
    poolConfig.setTestWhileIdle(true);
    poolConfig.setMinEvictableIdleTimeMillis(1000);
    poolConfig.setTimeBetweenEvictionRunsMillis(250);
    poolConfig.setValidationInterval(250);
    poolConfig.setLogAbandoned(true);

    //get a rpc client and return it immediately to the pool
    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);
    rpcClient.destroy();

    //wait for eviction idle time * 2 and check that the rpc client we get back is different then the old one
    Thread.sleep(2000);
    RPCClient anotherRpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);
    assertTrue("the first rpc client should have been evicted from the pool", rpcClient != anotherRpcClient);

  }


  @Test
  public void testResizePoolOnIdleRpcClients() throws Exception {

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();

    poolConfig.setInitialSize(5);
    poolConfig.setMaxActive(10);
    poolConfig.setMaxIdle(2);
    poolConfig.setTestWhileIdle(false);
    poolConfig.setMinIdle(2);
    poolConfig.setTimeBetweenEvictionRunsMillis(1000);
    poolConfig.setMinEvictableIdleTimeMillis(250);

    System.out.println(poolConfig.toString());

    //get a rpc client and return it immediately to the pool
    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);
    rpcClient.destroy();

    //check that the initial pool size is bigger then min idle
    PoolStatistics stat = PooledRPCClientFactory.getPoolStatistics(null, TEST_CHANNEL);
    assertNotNull("statistics for a pool not obtained", stat);
    assertTrue("there should be more then min idle slots in the pool", stat.getIdle() > 2);


    //wait for eviction idle time * 2 and check that the pool scaled back to min idle slots
    Thread.sleep(4000);

    assertThat("there should exactly min idle slots in the pool", stat.getIdle(), equalTo(2));

  }


  @Test
  public void testCheckAbandonRpcClient() throws Exception {

    PooledRPCClientFactory.resetConfiguration();

    PoolConfiguration poolConfig = PooledRPCClientFactory.getPoolConfiguration();

    poolConfig.setTimeBetweenEvictionRunsMillis(1000);
    poolConfig.setMinEvictableIdleTimeMillis(250);
    //busy rpc clients are abandoned after 1s
    poolConfig.setRemoveAbandonedTimeout(1);
    poolConfig.setRemoveAbandoned(true);
    poolConfig.setTestWhileIdle(false);
    poolConfig.setValidationInterval(250);
    System.out.println(poolConfig.toString());

    //get a rpc client
    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, TEST_CHANNEL);

    PoolStatistics stat = PooledRPCClientFactory.getPoolStatistics(null, TEST_CHANNEL);
    assertNotNull("statistics for a pool not obtained", stat);
    assertTrue("there should be 1 active rpc client", stat.getActive() == 1);

    //allow the time so that an active rpc client is abandoned
    Thread.sleep(4000);

    assertThat("there should be no active rpc client since one was abandoned", stat.getActive(), equalTo(0));

  }


  @Test(expected = IllegalArgumentException.class)
  public void testGetRpcClientWithNullChannelName() throws Exception {
    RPCClient rpcClient = PooledRPCClientFactory.getRPCClient(null, null);
  }


  @Override
  public void run() {
    try {
      this.rpcServer.run(0);
    } catch (PVAException ex) {
      Logger.getLogger(PooledRPCClientTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }


  static class TestRPCServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

    @Override
    public PVStructure request(PVStructure args) throws RPCRequestException {

      String methodName = args.getStringField("op").get();

      if ("sum".equals(methodName)) {
        return new SumServiceImpl().request(args);
      }

      return null;
    }
  }



  static class SumServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

    private final static Structure resultStructure =
      fieldCreate.createStructure(
        new String[]{"c"},
        new Field[]{fieldCreate.createScalar(ScalarType.pvDouble)});


    @Override
    public PVStructure request(PVStructure args) throws RPCRequestException {

      double a = args.getDoubleField("a").get();
      double b = args.getDoubleField("b").get();

      PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
      result.getDoubleField("c").put(a + b);

      return result;
    }
  }


  static class NopServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

    private final static Structure resultStructure =
      fieldCreate.createStructure(
        new String[0],
        new Field[0]);


    @Override
    public PVStructure request(PVStructure args) throws RPCRequestException {
      return PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
    }
  }


  @Before
  public void onSetUp() throws PVAException {
    this.rpcServer = new RPCServer();
    this.rpcServer.registerService(TEST_CHANNEL, new TestRPCServiceImpl());
    this.rpcServer.printInfo();
    Thread thread = new Thread(this);
    thread.start();
  }


  @After
  public void onTearDown() throws PVAException {
    PooledRPCClientFactory.close();
    this.rpcServer.destroy();
  }
}

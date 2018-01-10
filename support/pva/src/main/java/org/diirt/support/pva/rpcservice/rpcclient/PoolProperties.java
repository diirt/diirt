/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.diirt.support.pva.rpcservice.rpcclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Pool properties
 * @author Filip Hanik (tomcat connection pool)
 * @author dkumar (modified for a rpc client pool implementation)
 *
 */
class PoolProperties implements PoolConfiguration {

  /**
   * Logger
   */
  private final static Logger log = Logger.getLogger(PoolProperties.class.getName());


  protected static AtomicInteger poolCounter = new AtomicInteger(0);
  protected int initialSize = 10;
  protected int maxActive = DEFAULT_MAX_ACTIVE;
  protected int maxIdle = maxActive;
  protected int minIdle = initialSize;
  protected int maxWait = 30000;
  protected boolean testOnBorrow = false;
  protected boolean testOnReturn = false;
  protected boolean testWhileIdle = false;
  protected int timeBetweenEvictionRunsMillis = 5000;
  protected int numTestsPerEvictionRun;
  protected int minEvictableIdleTimeMillis = 60000;
  protected boolean removeAbandoned = false;
  protected int removeAbandonedTimeout = 60;
  protected boolean logAbandoned = false;
  protected long validationInterval = 30000;
  protected boolean testOnConnect =false;
  protected int abandonWhenPercentageFull = 0;
  protected long maxAge = 0;
  protected boolean useLock = false;
  protected int suspectTimeout = 0;


  /**
   * Create pool properties from text
   * @param textProperties properties in java properties format
   * @return pool configuration
   * @throws IOException
   */
  static PoolProperties createFromText(String textProperties) throws IOException {
    final Properties properties = new Properties();
    try(InputStream stream = new ByteArrayInputStream(textProperties.getBytes("UTF-8"))) {
      properties.load(stream);
    }
    return createFromProperties(properties);
  }


  private static PoolProperties createFromProperties(Properties properties) {

    PoolProperties poolProperties = new PoolProperties();

    Enumeration<Object> keys = properties.keys();

    while (keys.hasMoreElements()) {

      String key = (String)keys.nextElement();
      String keyValue = (String) properties.get(key);

      switch (key) {
        case "abandonWhenPercentageFull": poolProperties.setAbandonWhenPercentageFull(Integer.parseInt(keyValue));
          break;
        case "initialSize":  poolProperties.setInitialSize(Integer.parseInt(keyValue));
          break;
        case "logAbandoned":  poolProperties.setLogAbandoned(Boolean.parseBoolean(keyValue));
          break;
        case "maxActive":  poolProperties.setMaxActive(Integer.parseInt(keyValue));
          break;
        case "maxIdle": poolProperties.setMaxIdle(Integer.parseInt(keyValue));
          break;
        case "minIdle":  poolProperties.setMinIdle(Integer.parseInt(keyValue));
          break;
        case "maxWait":  poolProperties.setMaxWait(Integer.parseInt(keyValue));
          break;
        case "minEvictableIdleTimeMillis":  poolProperties.setMinEvictableIdleTimeMillis(Integer.parseInt(keyValue));
          break;
        case "removeAbandoned":  poolProperties.setRemoveAbandoned(Boolean.parseBoolean(keyValue));
          break;
        case "removeAbandonedTimeout": poolProperties.setRemoveAbandonedTimeout(Integer.parseInt(keyValue));
          break;
        case "testOnBorrow": poolProperties.setTestOnBorrow(Boolean.parseBoolean(keyValue));
          break;
        case "testOnReturn": poolProperties.setTestOnReturn(Boolean.parseBoolean(keyValue));
          break;
        case "testWhileIdle": poolProperties.setTestWhileIdle(Boolean.parseBoolean(keyValue));
          break;
        case "timeBetweenEvictionRunsMillis": poolProperties.setTimeBetweenEvictionRunsMillis(Integer.parseInt(keyValue));
          break;
        case "validationInterval": poolProperties.setValidationInterval(Integer.parseInt(keyValue));
          break;
        case "maxAge": poolProperties.setMaxAge(Integer.parseInt(keyValue));
          break;
        case "useLock": poolProperties.setUseLock(Boolean.parseBoolean(keyValue));
          break;
        case "suspectTimeout": poolProperties.setSuspectTimeout(Integer.parseInt(keyValue));
          break;
        default:
          log.log(Level.WARNING, "property " + key + " not supported");
          break;
      }
    }

    return poolProperties;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void setAbandonWhenPercentageFull(int percentage) {
    if (percentage<0) abandonWhenPercentageFull = 0;
    else if (percentage>100) abandonWhenPercentageFull = 100;
    else abandonWhenPercentageFull = percentage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAbandonWhenPercentageFull() {
    return abandonWhenPercentageFull;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getInitialSize() {
    return initialSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLogAbandoned() {
    return logAbandoned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxActive() {
    return maxActive;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxIdle() {
    return maxIdle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxWait() {
    return maxWait;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMinEvictableIdleTimeMillis() {
    return minEvictableIdleTimeMillis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMinIdle() {
    return minIdle;
  }


  /**
   * {@inheritDoc}
   */

  public int getNumTestsPerEvictionRun() {
    return numTestsPerEvictionRun;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRemoveAbandoned() {
    return removeAbandoned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getRemoveAbandonedTimeout() {
    return removeAbandonedTimeout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTestOnBorrow() {
    return testOnBorrow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTestOnReturn() {
    return testOnReturn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTestWhileIdle() {
    return testWhileIdle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTimeBetweenEvictionRunsMillis() {
    return timeBetweenEvictionRunsMillis;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public long getValidationInterval() {
    return validationInterval;
  }


  /**
   * {@inheritDoc}
   */

  public boolean isTestOnConnect() {
    return testOnConnect;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public void setInitialSize(int initialSize) {
    this.initialSize = initialSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLogAbandoned(boolean logAbandoned) {
    this.logAbandoned = logAbandoned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxActive(int maxActive) {
    this.maxActive = maxActive;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxIdle(int maxIdle) {
    this.maxIdle = maxIdle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxWait(int maxWait) {
    this.maxWait = maxWait;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
    this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMinIdle(int minIdle) {
    this.minIdle = minIdle;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void setRemoveAbandoned(boolean removeAbandoned) {
    this.removeAbandoned = removeAbandoned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
    this.removeAbandonedTimeout = removeAbandonedTimeout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTestOnBorrow(boolean testOnBorrow) {
    this.testOnBorrow = testOnBorrow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTestWhileIdle(boolean testWhileIdle) {
    this.testWhileIdle = testWhileIdle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTestOnReturn(boolean testOnReturn) {
    this.testOnReturn = testOnReturn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void setValidationInterval(long validationInterval) {
    this.validationInterval = validationInterval;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getSuspectTimeout() {
    return this.suspectTimeout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSuspectTimeout(int seconds) {
    this.suspectTimeout = seconds;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPoolSweeperEnabled() {
    boolean timer = getTimeBetweenEvictionRunsMillis()>0;
    boolean result = timer && (isRemoveAbandoned() && getRemoveAbandonedTimeout()>0);
    result = result || (timer && getSuspectTimeout()>0);
    result = result || (timer && isTestWhileIdle());
    result = result || (timer && getMinEvictableIdleTimeMillis()>0);
    return result;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public long getMaxAge() {
    return maxAge;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxAge(long maxAge) {
    this.maxAge = maxAge;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getUseLock() {
    return useLock;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUseLock(boolean useLock) {
    this.useLock = useLock;
  }


  @Override
  public String toString() {
    return "PoolProperties{" +
      "initialSize=" + initialSize +
      ", maxActive=" + maxActive +
      ", maxIdle=" + maxIdle +
      ", minIdle=" + minIdle +
      ", maxWait=" + maxWait +
      ", testOnBorrow=" + testOnBorrow +
      ", testOnReturn=" + testOnReturn +
      ", testWhileIdle=" + testWhileIdle +
      ", timeBetweenEvictionRunsMillis=" + timeBetweenEvictionRunsMillis +
      ", numTestsPerEvictionRun=" + numTestsPerEvictionRun +
      ", minEvictableIdleTimeMillis=" + minEvictableIdleTimeMillis +
      ", removeAbandoned=" + removeAbandoned +
      ", removeAbandonedTimeout=" + removeAbandonedTimeout +
      ", logAbandoned=" + logAbandoned +
      ", validationInterval=" + validationInterval +
      ", testOnConnect=" + testOnConnect +
      ", abandonWhenPercentageFull=" + abandonWhenPercentageFull +
      ", maxAge=" + maxAge +
      ", useLock=" + useLock +
      ", suspectTimeout=" + suspectTimeout +
      '}';
  }
}
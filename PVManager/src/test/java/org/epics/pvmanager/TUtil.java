/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import junit.framework.Assert;

/**
 *
 * @author carcassi
 */
public class TUtil {
    public static <T> Collector<T> createQueueCollector(Function<T> function, int maxValues) {
        return new QueueCollector<T>(function, maxValues);
    }
    
    public static ExceptionHandler failOnException() {
        return new ExceptionHandler() {

            @Override
            public void handleException(Exception ex) {
                Assert.fail("Exception thrown: " + ex.getMessage());
            }
            
        };
    }
}

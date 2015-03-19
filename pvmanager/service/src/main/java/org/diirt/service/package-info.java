/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

/**
 * Support for services (<a href="http://en.wikipedia.org/wiki/Request%E2%80%93response">request/response operations</a>).
 * <p>
 * There are three main objects a generic client will use. The {@link ServiceRegistry}
 * is used to locate the services and methods. The {@link Service} groups together
 * a set of methods that uses the same resources (i.e. thread pool, database
 * connections, ...). The {@link ServiceMethod} represents a single request/response
 * call, which can be executed both synchronously ({@link ServiceMethod#executeSync(java.util.Map) })
 * or asynchronously ({@link ServiceMethod#executeAsync(java.util.Map, java.util.function.Consumer, java.util.function.Consumer) }).
 * All objects are self-describing (i.e. have a name and description) and are
 * thread-safe.
 * <p>
 * When implementing a service, {@link ServiceMethodDescription} and
 * {@link ServiceDescription} are used to gather the information, so that
 * the final objects remain immutable and thread-safe. The actual implementation
 * of the call is provided by a subclass of {@link ServiceMethod}. The
 * {@link ServiceProvider} is used to register the service implementation
 * using the <code>ServiceLoader</code> mechanism.
 */
package org.diirt.service;

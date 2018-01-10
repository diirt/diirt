/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

/**
 * Support for services (<a href="http://en.wikipedia.org/wiki/Request%E2%80%93response">request/response operations</a>).
 * <p>
 * There are three main objects a generic client will use. The {@link org.diirt.service.ServiceRegistry}
 * is used to locate the services and methods. The {@link org.diirt.service.Service} groups together
 * a set of methods that use the same resources (i.e. thread pool, database
 * connections, ...). The {@link org.diirt.service.ServiceMethod} represents a single request/response
 * call, which can be executed both synchronously ({@link org.diirt.service.ServiceMethod#executeSync(java.util.Map)})
 * or asynchronously ({@link org.diirt.service.ServiceMethod#executeAsync(java.util.Map, java.util.function.Consumer, java.util.function.Consumer)}).
 * All objects are self-describing (i.e. have a name and description) and are
 * thread-safe.
 * <p>
 * When implementing a service, {@link org.diirt.service.ServiceMethodDescription} and
 * {@link org.diirt.service.ServiceDescription} are used to gather the information, so that
 * the final objects remain immutable and thread-safe. The actual implementation
 * of the call is provided by a subclass of {@link org.diirt.service.ServiceMethod}. The
 * {@link org.diirt.service.ServiceProvider} is used to register the service implementation
 * using the <code>ServiceLoader</code> mechanism.
 */
package org.diirt.service;

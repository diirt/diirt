/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

/**
 * Support for command/response type services.
 * <p>
 * A client can find services through the {@link ServiceRegistry}, which in turn uses the
 * <code>ServiceLoader</code> to find suitable {@link ServiceProvider}.
 * This means that adding the correct jar to the classpath is enough to enable
 * the support for the given service.
 * Once a service and a service method is identified, the client can execute
 * the method synchronously or asynchronously (preferred), with a call to
 * {@link ServiceMethod#executeAsync(java.util.Map, java.util.function.Consumer, java.util.function.Consumer) }
 * or {@link ServiceMethod#executeSync(java.util.Map) }. All objects are
 * thread-safe.
 * <p>
 * A service provide can create <code>ServiceMethod</code> implementations,
 * group them into {@link Service}s. The description classes are used to gather
 * data during service creation, so that the result remains immutable and thread-safe.
 * A <code>ServiceProvider</code> should be created as well, and properly
 * registered through the <code>ServiceLoader</code> so that all clients
 * can simply find the implementation through the registry.
 * 
 */
package org.diirt.service;

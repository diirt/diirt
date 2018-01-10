/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
/**
 * Support for Epics 3 data source (<a href="doc-files/jca-datasource.html">channel syntax</a>).
 * <p>
 * The {@link org.diirt.support.ca.JCADataSource} uses the {@link org.diirt.datasource.MultiplexedChannelHandler}. The
 * connection payload used is the JCA Channel class directly. The payload for
 * each monitor event is the {@link org.diirt.support.ca.JCAMessagePayload}, which includes both
 * metadata (taken with a GET at connection time) and value (taken from the
 * MONITOR event).
 * <p>
 * The conversion between JCAMessagePayload and the actual type, is done
 * through the {@link org.diirt.support.ca.JCATypeAdapter}. A JCATypeSupport can be passed
 * directly to the JCADataSource so that one can configure support for
 * different types.
 */
package org.diirt.support.ca;

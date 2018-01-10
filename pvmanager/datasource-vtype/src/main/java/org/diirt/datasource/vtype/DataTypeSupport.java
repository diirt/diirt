/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.diirt.vtype.VType;
import org.diirt.datasource.NotificationSupport;
import org.diirt.datasource.TypeSupport;

/**
 * Adds support for control system standard value types.
 *
 * @author carcassi
 */
public final class DataTypeSupport {

    private static volatile boolean installed = false;
    private static final Object installLock = new Object();

    /**
     * Installs type support. This should only be called by either DataSources
     * or ExpressionLanguage libraries that require support for these types.
     */
    public static void install() {
        // Install only once
        if (installed) {
            return;
        }

        // This looks like double-checked locking and in fact it is, but it is
        // safe because installed is volatile.
        // If we used an AtomicBoolean instead of the volatile boolean, we would
        // not need the lock (we could use a compare-and-set operation), but in
        // this case another thread calling this method might return before the
        // installation has actually finished.
        synchronized (installLock) {
            if (installed) {
                return;
            }

            // Add notification support for all immutable types
            TypeSupport.addTypeSupport(NotificationSupport.immutableTypeSupport(VType.class));

            installed = true;
        }
    }

    /**
     * Constructor.
     */
    private DataTypeSupport() {
        // Don't instantiate, utility class.
    }
}

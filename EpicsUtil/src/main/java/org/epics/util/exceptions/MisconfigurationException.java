/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.exceptions;

/**
 * Signals that the method is failing due to a misconfiguration of the
 * library which should be fixed by the user of such library.
 *
 * @author carcassi
 */
public class MisconfigurationException extends RuntimeException {

    public MisconfigurationException() {
    }
    
    public MisconfigurationException(String message) {
        super(message);
    }

    public MisconfigurationException(Throwable cause) {
        super(cause);
    }

    public MisconfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

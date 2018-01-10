/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

/**
 * Exception thrown when a {@link PVReader} or {@link PVWriter} exceed their
 * timeout.
 *
 * @author carcassi
 */
public class TimeoutException extends RuntimeException {

    TimeoutException(String message) {
        super(message);
    }

}

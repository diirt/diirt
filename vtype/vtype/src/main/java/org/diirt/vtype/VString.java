/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

/**
 * Scalar string with alarm and timestamp.
 *
 * @author carcassi
 */
public interface VString extends Scalar, Alarm, Time, VType {

    /**
     * {@inheritDoc }
     */
    @Override
    String getValue();
}

/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.next;

import org.diirt.vtype.*;

/**
 * Scalar integer with alarm, timestamp, display and control information.
 * Auto-unboxing makes the extra method for the primitive type
 * unnecessary.
 * 
 * @author carcassi
 */
public abstract class VInt extends VNumber {
    /**
     * {@inheritDoc }
     */
    @Override
    public abstract Integer getValue();
}
